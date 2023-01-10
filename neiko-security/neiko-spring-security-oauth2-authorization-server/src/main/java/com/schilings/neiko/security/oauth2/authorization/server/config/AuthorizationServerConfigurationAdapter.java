package com.schilings.neiko.security.oauth2.authorization.server.config;

import com.schilings.neiko.security.oauth2.authorization.server.*;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.DefaultOAuth2AuthorizationServerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.OAuth2AuthorizationServerExtensionConfigurerInjectionCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.authorization.DefaultOAuth2AuthorizationEndpointCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.introspection.DefaultOAuth2TokenIntrospectionCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.revocation.DefaultOAuth2TokenRevocationCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.OAuth2TokenEndpointExtensionGrantTypeCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.password.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.password.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 
 * <p></p>
 * 
 * @author Schilings
*/
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
class AuthorizationServerConfigurationAdapter implements SmartInitializingSingleton{

	private static final Log logger = LogFactory.getLog(AuthorizationServerConfigurationAdapter.class);

	@Autowired(required = false)
	private List<OAuth2AuthorizationServerInitializer> initializers;

	@Autowired(required = false)
	private List<OAuth2AuthorizationServerConfigurerCustomizer> configurerCustomizers;

	private final AuthenticationConfiguration authenticationConfiguration;

	private final ApplicationContext applicationContext;

	private final ObjectPostProcessor<Object> objectPostProcessor;

	private final HttpSecurity httpSecurity;

	private OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurity() throws Exception {
		HttpSecurity http = this.httpSecurity;
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = this.authorizationServerConfigurer;
		// 授权服务器配置customizer
		sortCustomizers();
		activeCustomizer(authorizationServerConfigurer, http);
		// build
		DefaultSecurityFilterChain filterChain = http.build();
		return filterChain;
	}
	

	@Override
	public void afterSingletonsInstantiated() {
		checkConfiguration();
		activeInitializers();
	}

	private void checkConfiguration() {
		Assert.notNull(this.httpSecurity, "HttpSecurity can not be null.");
		Assert.notNull(this.authenticationConfiguration, "AuthenticationConfiguration can not be null.");
		Assert.notNull(this.applicationContext, "ApplicationContext can not be null.");
		Assert.notNull(this.authorizationServerConfigurer, "AuthorizationServerConfigurer can not be null.");
	}

	private void activeInitializers() {
		if (!CollectionUtils.isEmpty(this.initializers)) {
			this.initializers.forEach(initializer -> initializer.initialize(this.httpSecurity));
		}
	}

	private void sortCustomizers() {
		if (!CollectionUtils.isEmpty(this.configurerCustomizers)) {
			AnnotationAwareOrderComparator.sort(this.configurerCustomizers);
		}
	}

	private void activeCustomizer(OAuth2AuthorizationServerConfigurer authorizationServerConfigurer, HttpSecurity http)
			throws Exception {
		if (!CollectionUtils.isEmpty(this.configurerCustomizers)) {
			for (OAuth2AuthorizationServerConfigurerCustomizer customizer : this.configurerCustomizers) {
				customizer.customize(authorizationServerConfigurer, http);
			}
		}
	}

	protected HttpSecurity getHttpSecurity() {
		return httpSecurity;
	}

	protected <P> P postProcess(P object) {
		return this.objectPostProcessor.postProcess(object);
	}

	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	protected OAuth2AuthorizationServerConfigurer getAuthorizationServerConfigurer() {
		return authorizationServerConfigurer;
	}

	protected AuthenticationConfiguration getAuthenticationConfiguration() {
		return authenticationConfiguration;
	}
	
}
