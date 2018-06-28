package com.github.corneil.test;

import com.github.corneil.config.JpaTransactionConfig;
import com.github.corneil.config.SpringDataJpaConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:/app-test.properties")
@Import({JpaTransactionConfig.class, SpringDataJpaConfig.class})
@ComponentScan("com.github.corneil.task")
public class TestConfig {
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
