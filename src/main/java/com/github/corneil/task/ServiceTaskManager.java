package com.github.corneil.task;

import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Corneil du Plessis.
 */
@XSlf4j
@Service
public class ServiceTaskManager implements ServiceTaskInterface {
	private RelationshipInterface relationshipService;

	@Autowired
	public ServiceTaskManager(RelationshipInterface relationshipService) {
		this.relationshipService = relationshipService;
	}

	@org.springframework.scheduling.annotation.Scheduled(initialDelay = 10000L, fixedRate = 60000L)
	public void checkExpired() {
		log.entry();
		int processed = relationshipService.checkExpired();
		log.info("checkExpired:processed:{}", processed);
		log.exit();
	}
}
