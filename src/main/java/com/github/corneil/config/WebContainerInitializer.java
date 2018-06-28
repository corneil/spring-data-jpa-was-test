package com.github.corneil.config;

import lombok.extern.slf4j.XSlf4j;
import org.springframework.util.Assert;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletRegistration;


@XSlf4j
public class WebContainerInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[]{AppConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{"/", "/*"};
	}


	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
		super.customizeRegistration(registration);
		log.entry(registration);
		Assert.notNull(registration);
		registration.setLoadOnStartup(1);
		log.exit();
	}
}
