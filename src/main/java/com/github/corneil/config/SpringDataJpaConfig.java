package com.github.corneil.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = {"com.github.corneil.model"})
@Import({DataSourceConfiguration.class})
@Slf4j
public class SpringDataJpaConfig {

	@Value("${database.type:DB2}")
	private String databaseType;

	@Value("${transaction.platform}")
	private String transactionPlatform;

	@Value("${hibernate.hbm2ddl.auto:validate}")
	private String hibernateHbm2ddl;

	@Bean(name = "entityManagerFactory")
	@Autowired
	public EntityManagerFactory entityManagerFactory(@Qualifier("dataSource") DataSource dataSource) {
		log.info("hibernateHbm2ddl={}", hibernateHbm2ddl);
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", hibernateHbm2ddl);
		properties.setProperty("hibernate.ejb.naming_strategy", org.hibernate.cfg.ImprovedNamingStrategy.class.getName());
		PersistenceUnitTransactionType transactionType = "JTA".equalsIgnoreCase(transactionPlatform) ? PersistenceUnitTransactionType.JTA : PersistenceUnitTransactionType.RESOURCE_LOCAL;
		log.info("additionalHibernateProperties:{}", properties);
		return JPAConfigHelper.create(
			"TestPersistenceUnit",
			getHibernateDialect(),
			transactionType,
			dataSource,
			properties,
			"com.github.corneil.model");
	}

	String getHibernateDialect() {
		if ("DB2".equals(databaseType)) {
			return "org.hibernate.dialect.DB2Dialect";
		} else if ("H2".equals(databaseType)) {
			return "org.hibernate.dialect.H2Dialect";
		} else if ("MySQL".equalsIgnoreCase(databaseType)) {
			return "org.hibernate.dialect.MySQLDialect";
		}
		return null;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
}
