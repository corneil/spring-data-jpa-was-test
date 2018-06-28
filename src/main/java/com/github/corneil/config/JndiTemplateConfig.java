package com.github.corneil.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jndi.JndiTemplate;

import javax.naming.NamingException;
import java.util.Properties;

@Configuration
public class JndiTemplateConfig {

    @Bean(name = "jndiTemplate")
    @Profile("WLP")
    public JndiTemplate wlpJndiTemplate() {
        return new JndiTemplate();
    }

    @Bean(name = "jndiTemplate")
    @Profile("WAS")
    public JndiTemplate wasJndiTemplate() throws NamingException {
        Properties environment = new Properties();
        environment.setProperty("java.naming.factory.initial", "com.ibm.websphere.naming.WsnInitialContextFactory");
        return new JndiTemplate(environment);
    }
}
