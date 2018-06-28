package com.github.corneil.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jndi.JndiTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.naming.NamingException;

/**
 * Created by bertram.julius on 2017/05/17.
 */
@Configuration
@Import(JndiTemplateConfig.class)
@EnableScheduling
public class SchedulerConfig {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);

    private static final String WLP_THREAD_POOL = "concurrent/scheduledExecutor";

    @Bean(name = "taskScheduler")
    @Profile("WLP")
    @Qualifier("jndiTemplate")
    @Autowired
    public TaskScheduler wlpScheduler(JndiTemplate jndiTemplate) {
        try {
            ManagedScheduledExecutorService managedScheduledExecutorService = jndiTemplate
                .lookup(WLP_THREAD_POOL, ManagedScheduledExecutorService.class);
            return new ConcurrentTaskScheduler(managedScheduledExecutorService);
        } catch (NamingException e) {
            logger.warn("Failed to resolve lookup for JNDI name: " + WLP_THREAD_POOL + ":" + e);
        }
        return new ConcurrentTaskScheduler();
    }

    @Bean(name = "taskScheduler")
    @Profile("!WLP")
    public TaskScheduler concurrentScheduler() throws NamingException {
        return new ConcurrentTaskScheduler();
    }
}
