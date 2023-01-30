package com.schilings.neiko.security.oauth2.authorization.server.autoconfigure;

import com.schilings.neiko.security.oauth2.authorization.server.HttpSecurityAwareInitializer;
import com.schilings.neiko.security.oauth2.authorization.server.properties.OAuth2AuthorizationServerProperties;

import com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2AuthorizationServerComponentUtils;
import com.schilings.neiko.security.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;

@AutoConfiguration
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnBean(AuthorizationServerConfigurationAdapter.class)
@EnableConfigurationProperties({OAuth2AuthorizationServerProperties.class,SecurityProperties.class})
@Import({DefaultCustomizerAutoConfiguration.class,
		DefaultExtensionConfigurerAutoConfiguration.class })
public class AuthorizationServerAutoConfiguration {

	@Bean
	public HttpSecurityAwareInitializer httpSecurityAwareInitializer() {
		return new HttpSecurityAwareInitializer();
	}
	
	@Bean
	public OAuth2AuthorizationServerComponentUtils oAuth2ComponentUtils() {
		return OAuth2AuthorizationServerComponentUtils.getInstance();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}
	
}
