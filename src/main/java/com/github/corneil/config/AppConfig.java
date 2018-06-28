package com.github.corneil.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan({"com.github.corneil.task", "com.github.corneil.rest"})
@PropertySource("classpath:/app.properties")
@Import({JtaTransactionConfig.class, JpaTransactionConfig.class, SpringDataJpaConfig.class, SchedulerConfig.class, ExecutorConfig.class})
public class AppConfig {
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
