package com.github.corneil.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.WebSphereUowTransactionManager;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;

@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@Profile("!JTA")
public class JpaTransactionConfig {

	@Profile("unit-test")
	@Bean(name = "transactionManager")
	@Autowired
	@Primary
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) throws NamingException {
		return new JpaTransactionManager(entityManagerFactory);
	}

	@Profile("unit-test")
	@Bean(name = "txManager")
	@Autowired
	public PlatformTransactionManager txManager(EntityManagerFactory entityManagerFactory) throws NamingException {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
