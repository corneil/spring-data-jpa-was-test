package com.github.corneil.rest;

import com.github.corneil.task.RelationshipInterface;
import com.github.corneil.task.ServiceTaskInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/schedule")
@RestController
@Slf4j
public class SchedulerContoller {
    RelationshipInterface relationshipInterface;

    public SchedulerContoller(RelationshipInterface relationshipInterface) {
        this.relationshipInterface = relationshipInterface;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> schedule() {
        try {
            relationshipInterface.checkExpired();
        } catch (Throwable x) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(x.toString());
        }
        return ResponseEntity.ok("Done");
    }
}
