package com.github.corneil.task;

import com.github.corneil.model.QRelationship;
import com.github.corneil.model.Relationship;
import com.github.corneil.model.RelationshipRepository;
import com.github.corneil.model.RelationshipStatus;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import lombok.extern.slf4j.XSlf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XSlf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RelationshipManager implements RelationshipInterface {
	Environment environment;
	RelationshipRepository relationshipRepository;

	public RelationshipManager(Environment environment, RelationshipRepository relationshipRepository) {
		this.environment = environment;
		this.relationshipRepository = relationshipRepository;
	}

	public DateTime determineExpiryTime(String propertyName, int defaultDays) {
		log.entry(propertyName, defaultDays);
		String abandonTimeout = environment.getProperty(propertyName);
		int abandonDays = abandonTimeout != null ? Integer.parseInt(abandonTimeout) : defaultDays;
		DateTime earliestDraft = DateTime.now().minusDays(abandonDays);
		return log.exit(earliestDraft);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int checkExpired() {
		log.info("checkExpired:start");
		int expiredPending = 0;
		int expiredDraft = 0;
		int archived = 0;
		DateTime earliestDraft = determineExpiryTime("timeout.abandon", 7);
		DateTime reminderPending = determineExpiryTime("timeout.reminder", 5);
		DateTime earliestPending = determineExpiryTime("timeout.reject", 14);
		DateTime otherPending = determineExpiryTime("timeout.archive", 90);
		DateTime abandonDate = DateTime.now().plus(new Duration(earliestDraft, reminderPending));
		QRelationship rel = QRelationship.relationship;
		BooleanExpression draftExpired = rel.status.eq(RelationshipStatus.DRAFT).and(rel.createdDate.before(earliestDraft.toDate()));
		BooleanExpression pendingExpired = rel.status.eq(RelationshipStatus.PENDING).and(rel.submittedDate.before(earliestPending.toDate()));
		BooleanExpression pendingReminder = rel.status.eq(RelationshipStatus.PENDING)
			.and(rel.submittedDate.before(reminderPending.toDate()))
			.and(rel.reminderSent.eq(true).not());
		List<RelationshipStatus> toArchive = Arrays.asList(RelationshipStatus.REJECTED,
			RelationshipStatus.ABANDONED,
			RelationshipStatus.AUTO_REJECTED,
			RelationshipStatus.CANCELLED);
		BooleanExpression otherExpired = rel.status.in(toArchive).and(rel.endDate.before(otherPending.toDate()));
		List<Relationship> modified = new ArrayList<>();
		Date now = new Date();
		Predicate predicate = rel.archived.eq(false).and(BooleanExpression.anyOf(draftExpired, pendingExpired, pendingReminder, otherExpired));
		for (Relationship expired : relationshipRepository.findAll(predicate)) {
			switch (expired.getStatus()) {
				case DRAFT:
					expired.setStatus(RelationshipStatus.ABANDONED);
					expired.setEndDate(now);
					modified.add(expired);
					expiredDraft += 1;
					log.debug("checkExpired:abandon:id:{}:created:{}", expired.getId(), LocalDateTime.fromDateFields(expired.getCreatedDate()));
					break;
				case PENDING:
					if (expired.getSubmittedDate().before(earliestPending.toDate())) {
						expired.setStatus(RelationshipStatus.AUTO_REJECTED);
						expired.setEndDate(now);
						modified.add(expired);
						expiredPending += 1;
						log.debug("checkExpired:reject:id:{}:submitted:{}", expired.getId(), LocalDateTime.fromDateFields(expired.getSubmittedDate()));
					} else if (expired.getSubmittedDate().before(reminderPending.toDate())) {
						log.debug("checkExpired:reminder:id:{}:createDate={}:expiryDate={}",
							expired.getId(),
							LocalDateTime.fromDateFields(expired.getCreatedDate()),
							abandonDate);
						Map<String, String> parameters = new HashMap<>();
						parameters.put("AbandonDate", ISODateTimeFormat.date().print(abandonDate));
						log.info("send notification-reminder:{}", expired.getId());
						expired.setReminderSent(true);
						modified.add(expired);
					} else {
						log.error("Unexpected PENDING:{}:createDate={}:submitDate={}:reminderDate={}:expiryDate={}",
							expired.getId(),
							LocalDateTime.fromDateFields(expired.getCreatedDate()),
							LocalDateTime.fromDateFields(expired.getSubmittedDate()),
							reminderPending,
							earliestPending);
					}
					break;
				case CANCELLED:
				case ABANDONED:
				case AUTO_REJECTED:
				case REJECTED:
					log.debug("checkExpired:archive:id:{}:status:{}:end-date:{}",
						expired.getId(),
						expired.getStatus(),
						LocalDateTime.fromDateFields(expired.getEndDate()).toString());
					expired.setArchived(true);
					expired.setArchivedDate(now);
					modified.add(expired);
					archived += 1;
					break;
				default:
					log.error("checkExpired:id:{}:unexpected state:{}:relationship:{}", expired.getId(), expired.getStatus(), expired);
			}
			if (modified.size() > 100) {
				break;
			}
		}
		relationshipRepository.save(modified);
		if (expiredDraft > 0) {
			log.debug("checkExpired:expiredDraft:{}", expiredDraft);
		}
		if (expiredPending > 0) {
			log.debug("checkExpired:expiredPending:{}", expiredPending);
		}
		if (archived > 0) {
			log.debug("checkExpired:archive:{}", archived);
		}
		return log.exit(expiredDraft + expiredPending + archived);
	}
}
