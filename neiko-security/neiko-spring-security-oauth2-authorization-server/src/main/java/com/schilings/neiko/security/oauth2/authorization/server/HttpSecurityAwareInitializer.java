package com.schilings.neiko.security.oauth2.authorization.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class HttpSecurityAwareInitializer implements OAuth2AuthorizationServerInitializer {

	private HttpSecurity httpSecurity;

	private ConfigurableApplicationContext applicationContext;

	@Override
	public void initialize(HttpSecurity httpSecurity) {
		this.httpSecurity = httpSecurity;
		this.applicationContext = (ConfigurableApplicationContext) httpSecurity
				.getSharedObject(ApplicationContext.class);

		ApplicationContext applicationContext = this.applicationContext;
		String[] namesForType = applicationContext.getBeanNamesForType(HttpSecurityAware.class);
		for (String beanName : namesForType) {
			Object bean = applicationContext.getBean(beanName);
			AccessControlContext acc = null;
			if (System.getSecurityManager() != null) {
				acc = this.applicationContext.getBeanFactory().getAccessControlContext();
			}

			if (acc != null) {
				AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
					invokeAwareInterfaces(bean);
					return null;
				}, acc);

			}
			else {
				invokeAwareInterfaces(bean);
			}
		}
	}

	private void invokeAwareInterfaces(Object bean) {
		if (bean instanceof HttpSecurityAware) {
			((HttpSecurityAware) bean).setHttpSecurity(this.httpSecurity);
		}
	}

}
