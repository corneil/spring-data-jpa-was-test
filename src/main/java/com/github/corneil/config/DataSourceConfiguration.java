package com.github.corneil.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.util.Assert;

import javax.sql.DataSource;

/**
 * @author Corneil du Plessis
 */
@Configuration
@Slf4j
public class DataSourceConfiguration {
	@Autowired
	private Environment environment;
	@Value("${database.type:DB2}")
	private String databaseType;
	@Value("${database.embedded:true}")
	private String embeddedDatabase;

	@Bean(name = "dataSource")
	public DataSource createDataSource() {
		if (Boolean.parseBoolean(embeddedDatabase)) {
			Assert.notNull(databaseType, "databaseType is required. Define property named: database.type");
			BasicDataSource dataSource = new BasicDataSource();
			StringBuilder builder = new StringBuilder();
			builder.append("jdbc:h2:mem:test;");
			if (!"H2".equals(databaseType)) {
				builder.append("MODE=");
				builder.append(databaseType);
				builder.append(";");
			}
			builder.append("IGNORECASE=TRUE;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;INIT=CREATE SCHEMA IF NOT EXISTS TEST");
			String url = builder.toString();
			dataSource.setUrl(url);
			dataSource.setUsername("sa");
			dataSource.setPassword("");
			dataSource.setDriverClassName("org.h2.Driver");
			return dataSource;
		} else {
			JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
			dataSourceLookup.setResourceRef(true);
			return dataSourceLookup.getDataSource("jdbc/test-ds");
		}
	}
}
