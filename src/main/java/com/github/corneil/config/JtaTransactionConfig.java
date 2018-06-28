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
@Import(JndiTemplateConfig.class)
@Profile("JTA")
public class JtaTransactionConfig {

	@Profile("WLP")
	@Bean(name = "transactionManager")
	@Qualifier("jndiTemplate")
	@Autowired
	@Primary
	public PlatformTransactionManager transactionManager(JndiTemplate jndiTemplate) throws NamingException {
		WebSphereUowTransactionManager webSphereUowTransactionManager = new WebSphereUowTransactionManager();
		webSphereUowTransactionManager.setJndiTemplate(jndiTemplate);
		webSphereUowTransactionManager.afterPropertiesSet();
		return webSphereUowTransactionManager;
	}

	@Profile("WLP")
	@Bean(name = "txManager")
	@Qualifier("jndiTemplate")
	@Autowired
	public PlatformTransactionManager txManager(JndiTemplate jndiTemplate) throws NamingException {
		System.err.println("=================TXMANAGER-CREATED==================");
		WebSphereUowTransactionManager webSphereUowTransactionManager = new WebSphereUowTransactionManager();
		webSphereUowTransactionManager.setJndiTemplate(jndiTemplate);
		webSphereUowTransactionManager.afterPropertiesSet();
		return webSphereUowTransactionManager;
	}
}
