package com.schilings.neiko.security.oauth2.authorization.server.config;

import com.schilings.neiko.security.oauth2.authorization.server.HttpSecurityAwareInitializer;
import com.schilings.neiko.security.oauth2.authorization.server.NullEventAuthenticationFailureHandler;
import com.schilings.neiko.security.oauth2.authorization.server.NullEventAuthenticationSuccessHandler;
import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerExtensionConfigurer;
import com.schilings.neiko.security.oauth2.authorization.server.configurer.LastTriggeredAuthenticatedConfigurer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.DefaultOAuth2AuthorizationServerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.OAuth2AuthorizationServerExtensionConfigurerInjectionCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.authorization.DefaultOAuth2AuthorizationEndpointCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.introspection.DefaultOAuth2TokenIntrospectionCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.oidc.DefaultOAuth2OidcConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.revocation.DefaultOAuth2TokenRevocationCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.OAuth2TokenEndpointExtensionGrantTypeCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityAuthenticationConverter;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityAuthenticationProvider;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.password.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.password.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2ComponentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnBean(AuthorizationServerConfigurationAdapter.class)
@Import({ AuthorizationServerAutoConfiguration.DefaultCustomizerAutoConfiguration.class,
		AuthorizationServerAutoConfiguration.DefaultExtensionConfigurerAutoConfiguration.class })
class AuthorizationServerAutoConfiguration {

	@Bean
	public HttpSecurityAwareInitializer httpSecurityAwareInitializer() {
		return new HttpSecurityAwareInitializer();
	}

	@Bean
	@ConditionalOnMissingBean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}

	@Bean
	public OAuth2ComponentUtils oAuth2ComponentUtils() {
		return new OAuth2ComponentUtils();
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @author Schilings
	 */
	@Configuration(proxyBeanMethods = false)
	static class DefaultCustomizerAutoConfiguration {

		/**
		 * Custom Configurer Injection
		 * @param configurers
		 * @return
		 */
		@Bean
		public OAuth2AuthorizationServerExtensionConfigurerInjectionCustomizer extensionConfigurerInjectionCustomizer(
				@Autowired(required = false) List<OAuth2AuthorizationServerExtensionConfigurer> configurers) {
			return new OAuth2AuthorizationServerExtensionConfigurerInjectionCustomizer(configurers);
		}

		/**
		 * Default Configuration
		 * @return
		 */
		@Bean
		@ConditionalOnMissingBean
		public DefaultOAuth2AuthorizationServerCustomizer defaultOAuth2AuthorizationServerCustomizer() {
			return new DefaultOAuth2AuthorizationServerCustomizer();
		}

		/**
		 * Authorization Endpoint
		 * @return
		 */
		@Bean
		@ConditionalOnMissingBean
		public DefaultOAuth2AuthorizationEndpointCustomizer defaultAuthorizationEndpointCustomizer() {
			DefaultOAuth2AuthorizationEndpointCustomizer authorizationCustomizer = new DefaultOAuth2AuthorizationEndpointCustomizer();
			authorizationCustomizer.authorizationResponseHandler(NullEventAuthenticationSuccessHandler::new);
			authorizationCustomizer.errorResponseHandler(NullEventAuthenticationFailureHandler::new);
			return authorizationCustomizer;
		}

		/**
		 * Token Endpoint
		 * @return
		 */
		@Bean
		@ConditionalOnMissingBean
		public OAuth2TokenEndpointExtensionGrantTypeCustomizer extensionGrantTypeCustomizer() {
			OAuth2TokenEndpointExtensionGrantTypeCustomizer extensionGrantTypeCustomizer = new OAuth2TokenEndpointExtensionGrantTypeCustomizer();
			// converter
			extensionGrantTypeCustomizer.converterExpander(((converters, http) -> {
				converters.add(new OAuth2ResourceOwnerPasswordAuthenticationConverter());
			}));
			// provider
			extensionGrantTypeCustomizer.providerExpander((providers, authorizationService, tokenGenerator, http) -> {
				// 未build,so 懒加载
				AuthenticationManager authenticationManager = authentication -> http
						.getSharedObject(AuthenticationManager.class).authenticate(authentication);
				providers.add(new OAuth2ResourceOwnerPasswordAuthenticationProvider(authenticationManager,
						authorizationService, tokenGenerator));
			});
			// handler
			extensionGrantTypeCustomizer.accessTokenResponseHandler(NullEventAuthenticationSuccessHandler::new);
			extensionGrantTypeCustomizer.errorResponseHandler(NullEventAuthenticationFailureHandler::new);
			return extensionGrantTypeCustomizer;
		}

		/**
		 * Token Revocation Endpoint
		 * @return
		 */
		@Bean
		@ConditionalOnMissingBean
		public DefaultOAuth2TokenRevocationCustomizer defaultTokenRevocationCustomizer() {
			DefaultOAuth2TokenRevocationCustomizer tokenRevocationCustomizer = new DefaultOAuth2TokenRevocationCustomizer();
			tokenRevocationCustomizer.revocationResponseHandler(NullEventAuthenticationSuccessHandler::new);
			tokenRevocationCustomizer.errorResponseHandler(NullEventAuthenticationFailureHandler::new);
			return tokenRevocationCustomizer;
		}

		/**
		 * Token Introspection Endpoint
		 * @return
		 */
		@Bean
		@ConditionalOnMissingBean
		public DefaultOAuth2TokenIntrospectionCustomizer defaultOAuth2TokenIntrospectionCustomizer() {
			DefaultOAuth2TokenIntrospectionCustomizer tokenIntrospectionCustomizer = new DefaultOAuth2TokenIntrospectionCustomizer();
			tokenIntrospectionCustomizer.revocationResponseHandler(NullEventAuthenticationSuccessHandler::new);
			tokenIntrospectionCustomizer.errorResponseHandler(NullEventAuthenticationFailureHandler::new);
			return tokenIntrospectionCustomizer;
		}

		/**
		 * Enable OpenId Connect
		 * @return
		 */
		@Bean
		@ConditionalOnMissingBean
		public DefaultOAuth2OidcConfigurerCustomizer oidcConfigurerCustomizer() {
			return new DefaultOAuth2OidcConfigurerCustomizer();
		}

	}

	/**
	 * <p>
	 * </p>
	 *
	 * @author Schilings
	 */
	@Configuration(proxyBeanMethods = false)
	static class DefaultExtensionConfigurerAutoConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public LastTriggeredAuthenticatedConfigurer lastTriggeredAuthenticatedConfigurer() {
			return new LastTriggeredAuthenticatedConfigurer();
		}

	}

}
