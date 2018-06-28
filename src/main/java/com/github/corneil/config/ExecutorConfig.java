package com.github.corneil.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jndi.JndiTemplate;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import javax.enterprise.concurrent.ManagedExecutorService;
import javax.naming.NamingException;

/**
 * Created by bertram.julius on 2017/05/17.
 */
@Configuration
@Import({JndiTemplateConfig.class})
@Slf4j
public class ExecutorConfig {
    private static final String WLP_THREAD_POOL = "concurrent/executor";

    @Bean(name = "executor")
    @Profile("WLP")
    @Qualifier("jndiTemplate")
    @Autowired
    public TaskExecutor wlpExecutor(JndiTemplate jndiTemplate) {
        try {
            ManagedExecutorService managedExecutorService = jndiTemplate.lookup(WLP_THREAD_POOL, ManagedExecutorService.class);
            return new ConcurrentTaskExecutor(managedExecutorService);
        } catch (NamingException e) {
            log.warn("Failed to resolve lookup for JNDI name: " + WLP_THREAD_POOL + ":" + e);
        }
        return new ConcurrentTaskExecutor();
    }

    @Bean(name = "executor")
    @Profile("!WLP")
    public TaskExecutor concurrentExecutor() {
        return new ConcurrentTaskExecutor();
    }

}
