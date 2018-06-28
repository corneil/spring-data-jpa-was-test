package com.github.corneil.rest;

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
    private ServiceTaskInterface taskInterface;

    public SchedulerContoller(ServiceTaskInterface taskInterface) {
        this.taskInterface = taskInterface;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> schedule() {
        try {
            taskInterface.checkExpired();
        } catch (Throwable x) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(x.toString());
        }
        return ResponseEntity.ok("Done");
    }
}
