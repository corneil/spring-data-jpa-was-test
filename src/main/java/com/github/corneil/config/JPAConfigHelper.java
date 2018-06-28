package com.github.corneil.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
public class JPAConfigHelper {


	public static EntityManagerFactory create(DefaultPersistenceUnitManager defaultPersistenceUnitManager, String persistenceUnit, String dialect) {
		return create(defaultPersistenceUnitManager, persistenceUnit, dialect, null, null);
	}

	public static EntityManagerFactory create(DefaultPersistenceUnitManager defaultPersistenceUnitManager, String persistenceUnit, String dialect, String... packagesToScan) {
		log.debug("create:{}:{}", persistenceUnit, dialect);
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setPersistenceUnitManager(defaultPersistenceUnitManager);
		em.setPersistenceUnitName(persistenceUnit);
		em.setJpaProperties(additionalProperties(PersistenceUnitTransactionType.JTA, dialect, null));
		em.setPersistenceProviderClass(HibernatePersistence.class);
		if (packagesToScan != null) {
			em.setPackagesToScan(packagesToScan);
		}
		em.afterPropertiesSet();

		return em.getObject();
	}

	public static EntityManagerFactory create(String persistenceUnit, String dialect, PersistenceUnitTransactionType transactionType, DataSource dataSource, Properties hibernateProperties, String... packagesToScan) {
		log.debug("create:{}:{}:{}:{}:{}", persistenceUnit, dialect, transactionType, hibernateProperties, packagesToScan);
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setJpaProperties(additionalProperties(transactionType, dialect, hibernateProperties));
		em.setPersistenceProviderClass(HibernatePersistence.class);
		em.setJpaVendorAdapter(jpaVendorAdapter(dialect));
		em.setJpaDialect(new HibernateJpaDialect());
		em.setPersistenceUnitName(persistenceUnit);
		if (packagesToScan != null) {
			em.setPackagesToScan(packagesToScan);
		}
		if (transactionType != null && dataSource != null) {
			if (PersistenceUnitTransactionType.JTA.equals(transactionType)) {
				em.setJtaDataSource(dataSource);
			} else {
				em.setDataSource(dataSource);
			}
		}
		em.afterPropertiesSet();

		return em.getObject();
	}

	private static HibernateJpaVendorAdapter jpaVendorAdapter(String dialect) {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		if (dialect != null) {
			if (dialect.contains(".DB2")) {
				jpaVendorAdapter.setDatabase(Database.DB2);
			} else if (dialect.contains(".SQLServer")) {
				jpaVendorAdapter.setDatabase(Database.SQL_SERVER);
			} else if (dialect.contains(".H2")) {
				jpaVendorAdapter.setDatabase(Database.H2);
			} else if (dialect.contains(".MySQL")) {
				jpaVendorAdapter.setDatabase(Database.MYSQL);
			}
		}
		return jpaVendorAdapter;
	}

	private static Properties additionalProperties(PersistenceUnitTransactionType transactionType, String dialect, Properties hibernateProperties) {
		Properties jpaProps = new Properties();
		if (PersistenceUnitTransactionType.JTA.equals(transactionType)) {
			jpaProps.put("hibernate.transaction.jta.platform", "org.hibernate.service.jta.platform.internal.WebSphereExtendedJtaPlatform");
			// jpaProps.put("hibernate.transaction.jta.platform", "org.hibernate.service.jta.platform.internal.WebSphereJtaPlatform");
		}
		if (dialect != null) {
			jpaProps.put("hibernate.dialect", dialect);
		}
		if (hibernateProperties != null) {
			for (String name : hibernateProperties.stringPropertyNames()) {
				jpaProps.put(name, hibernateProperties.getProperty(name));
			}
		}
		log.debug("additionalProperties:{}", jpaProps);
		return jpaProps;
	}
}
