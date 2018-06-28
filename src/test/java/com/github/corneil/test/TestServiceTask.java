package com.github.corneil.test;

import com.github.corneil.model.Relationship;
import com.github.corneil.model.RelationshipRepository;
import com.github.corneil.model.RelationshipStatus;
import com.github.corneil.task.RelationshipInterface;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestConfig.class)
@ActiveProfiles("unit-test")
public class TestServiceTask {
	@Autowired
	RelationshipInterface relationshipInterface;

	@Autowired
	RelationshipRepository relationshipRepository;

	private Relationship createRelationship(RelationshipStatus status, Date createdDate) {
		Relationship relationship = new Relationship();
		relationship.setCreatedDate(createdDate);
		relationship.setStatus(status);
		return relationship;
	}

	@Test
	public void testExpired() {
		// given
		Relationship relationship = createRelationship(RelationshipStatus.PENDING, LocalDateTime.now().minusDays(10).toDate());
		relationship.setSubmittedDate(LocalDateTime.now().minusDays(9).toDate());
		relationshipRepository.save(relationship);
		System.out.println("relationship=" + relationship);
		// when
		int expired = relationshipInterface.checkExpired();
		// then
		System.out.println("expired=" + expired);
		Assert.assertTrue(expired > 0);
	}
}
