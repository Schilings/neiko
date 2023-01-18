package com.schilings.neiko.security.oauth2.resource.server.config;

import com.schilings.neiko.security.oauth2.resource.server.OAuth2ResourceServerConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.resource.server.exception.DefaultAccessDeniedHandler;
import com.schilings.neiko.security.oauth2.resource.server.exception.DefaultAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
class ResourceServerConfigurationAdapter implements SmartInitializingSingleton {

	private static final Log logger = LogFactory.getLog(ResourceServerConfigurationAdapter.class);

	@Autowired(required = false)
	private List<OAuth2ResourceServerConfigurerCustomizer> configurerCustomizers;

	private final ApplicationContext applicationContext;

	private final AuthenticationConfiguration authenticationConfiguration;

	private final ObjectPostProcessor<Object> objectPostProcessor;

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE + 99)
	public SecurityFilterChain resourceServerSecurity(HttpSecurity http) throws Exception {
		// 资源服务器配置customizer
		sortCustomizers();
		activeCustomizer(http);
		// build
		DefaultSecurityFilterChain filterChain = http.build();
		return filterChain;
	}
	
	@Override
	public void afterSingletonsInstantiated() {
		checkConfiguration();
	}

	private void checkConfiguration() {
		Assert.notNull(this.authenticationConfiguration, "AuthenticationConfiguration can not be null.");
		Assert.notNull(this.applicationContext, "ApplicationContext can not be null.");
	}

	private void sortCustomizers() {
		if (!CollectionUtils.isEmpty(this.configurerCustomizers)) {
			AnnotationAwareOrderComparator.sort(this.configurerCustomizers);
		}
	}

	private void activeCustomizer(HttpSecurity http) throws Exception {
		if (!CollectionUtils.isEmpty(this.configurerCustomizers)) {
			for (OAuth2ResourceServerConfigurerCustomizer customizer : this.configurerCustomizers) {
				customizer.customize(http);
			}
		}
	}

	protected <P> P postProcess(P object) {
		return this.objectPostProcessor.postProcess(object);
	}

	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	protected AuthenticationConfiguration getAuthenticationConfiguration() {
		return authenticationConfiguration;
	}

}