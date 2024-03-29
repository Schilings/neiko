package com.schilings.neiko.security.oauth2.resource.server.autoconfigure;

import com.schilings.neiko.security.oauth2.resource.server.customizer.jwt.DefaultJwtAuthenticationConverter;
import com.schilings.neiko.security.oauth2.resource.server.customizer.opaque.DefaultOpaqueTokenAuthenticationConverter;
import com.schilings.neiko.security.oauth2.resource.server.customizer.opaque.SpringAuthorizationServerSharedStoredOpaqueTokenIntrospector;
import com.schilings.neiko.security.oauth2.resource.server.properties.ResourceServerProperties;
import com.schilings.neiko.security.oauth2.resource.server.customizer.opaque.SpringRemoteOpaqueTokenIntrospector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

class ResourceServerConfiguration {

	/**
	 * @see org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerJwtConfiguration
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(JwtDecoder.class)
	static class JwtConfiguration {

		private final ResourceServerProperties.Jwt properties;

		JwtConfiguration(ResourceServerProperties properties) {
			this.properties = properties.getJwt();
		}

		/**
		 * Process the authenticated Jwt into local AuthenticationToken
		 * @return
		 */
		@Bean
		@ConditionalOnMissingBean(JwtAuthenticationConverter.class)
		public DefaultJwtAuthenticationConverter defaultJwtAuthenticationConverter() {
			return new DefaultJwtAuthenticationConverter();
		}

	}

	/**
	 * @see org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerOpaqueTokenConfiguration
	 */
	@Configuration(proxyBeanMethods = false)
	static class OpaqueTokenConfiguration {

		private final ResourceServerProperties.Opaquetoken properties;

		public OpaqueTokenConfiguration(ResourceServerProperties properties) {
			this.properties = properties.getOpaquetoken();
		}

		/**
		 * Process the authenticated OpaqueToken into local AuthenticationToken
		 * @return
		 */
		@Bean
		@ConditionalOnMissingBean(OpaqueTokenAuthenticationConverter.class)
		public DefaultOpaqueTokenAuthenticationConverter defaultOpaqueTokenAuthenticationConverter() {
			return new DefaultOpaqueTokenAuthenticationConverter();
		}

		/**
		 * In case where is Spring Authorization Server,consider the way of Shared Stored
		 * without introspection-uri configured
		 * @param applicationContext
		 * @return
		 */
		@Bean
		@ConditionalOnClass({ OAuth2AuthorizationServerConfigurer.class })
		@Conditional(SharedStoredCondition.class)
		@ConditionalOnBean({ OAuth2AuthorizationService.class, RegisteredClientRepository.class })
		@ConditionalOnMissingBean(OpaqueTokenIntrospector.class)
		public OpaqueTokenIntrospector sharedStoredOpaqueTokenIntrospector(ApplicationContext applicationContext) {
			OAuth2AuthorizationService authorizationService = applicationContext
					.getBean(OAuth2AuthorizationService.class);
			RegisteredClientRepository registeredClientRepository = applicationContext
					.getBean(RegisteredClientRepository.class);
			return new SpringAuthorizationServerSharedStoredOpaqueTokenIntrospector(authorizationService,
					registeredClientRepository);
		}

		/**
		 * If introspection-uri configured, then SpringRemoteOpaqueTokenIntrospector
		 * injects.
		 * @return
		 */
		@Bean
		@ConditionalOnMissingBean(OpaqueTokenIntrospector.class)
		@ConditionalOnProperty(name = "neiko.security.oauth2.resourceserver.opaquetoken.introspection-uri")
		public OpaqueTokenIntrospector remoteOpaqueTokenIntrospector() {
			return new SpringRemoteOpaqueTokenIntrospector(properties.getIntrospectionUri(), properties.getClientId(),
					properties.getClientSecret());
		}

	}

}
