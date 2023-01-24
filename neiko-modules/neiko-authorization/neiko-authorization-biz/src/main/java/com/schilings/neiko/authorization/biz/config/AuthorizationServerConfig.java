package com.schilings.neiko.authorization.biz.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.schilings.neiko.authorization.biz.RegisteredClientPropertiesMapper;
import com.schilings.neiko.authorization.biz.handler.OAuth2AccessTokenAuthenticationSuccessHandler;
import com.schilings.neiko.authorization.biz.handler.OAuth2TokenRevocationAuthenticationSuccessHandler;
import com.schilings.neiko.authorization.biz.jose.Jwks;
import com.schilings.neiko.authorization.common.properties.SecurityProperties;
import com.schilings.neiko.oauth2.core.ScopeNames;
import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.config.EnableAuthorizationServer;
import com.schilings.neiko.security.oauth2.authorization.server.configurer.OAuth2LoginPasswordDecoderConfigurer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.OAuth2AuthorizationEndpointConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.OAuth2TokenRevocationEndpointConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.authorization.DefaultOAuth2AuthorizationEndpointCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.revocation.DefaultOAuth2TokenRevocationCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.OAuth2TokenEndpointExtensionGrantTypeCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityAuthenticationConverter;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityAuthenticationProvider;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityConstant;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.password.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.password.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.util.UUID;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig {

	@Bean
	@ConditionalOnMissingBean
	public RegisteredClientRepository registeredClientRepository(RegisteredClientPropertiesMapper mapper) {
		return new InMemoryRegisteredClientRepository(mapper.getRegisteredClients());
	}

	@Bean
	@ConditionalOnMissingBean
	public OAuth2AuthorizationService authorizationService() {
		return new InMemoryOAuth2AuthorizationService();
	}

	@Bean
	@ConditionalOnMissingBean
	public OAuth2AuthorizationConsentService authorizationConsentService() {
		return new InMemoryOAuth2AuthorizationConsentService();
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = "neiko.security.password-secret-key", matchIfMissing = false)
	public OAuth2LoginPasswordDecoderConfigurer loginPasswordDecoderConfigurer(SecurityProperties securityProperties) {
		return new OAuth2LoginPasswordDecoderConfigurer(securityProperties.getPasswordSecretKey());
	}

	@Bean
	@ConditionalOnMissingBean
	public OAuth2AuthorizationServerConfigurerCustomizer extensionGrantTypeCustomizer() {
		OAuth2TokenEndpointExtensionGrantTypeCustomizer extensionGrantTypeCustomizer = new OAuth2TokenEndpointExtensionGrantTypeCustomizer();
		extensionGrantTypeCustomizer.converterExpander(((converters, http) -> {
			converters.add(new OAuth2ResourceOwnerPasswordAuthenticationConverter());
			converters.add(new OAuth2FederatedIdentityAuthenticationConverter());
		}));
		extensionGrantTypeCustomizer.providerExpander((providers, authorizationService, tokenGenerator, http) -> {
			AuthenticationManager authenticationManager = authentication -> http
					.getSharedObject(AuthenticationManager.class).authenticate(authentication);
			providers.add(new OAuth2ResourceOwnerPasswordAuthenticationProvider(authenticationManager,
					authorizationService, tokenGenerator));
			providers.add(new OAuth2FederatedIdentityAuthenticationProvider(authorizationService, tokenGenerator));
		});
		extensionGrantTypeCustomizer.accessTokenResponseHandler(OAuth2AccessTokenAuthenticationSuccessHandler::new);
		return extensionGrantTypeCustomizer;
	}

	@Bean
	@ConditionalOnMissingBean
	public OAuth2AuthorizationServerConfigurerCustomizer tokenRevocationCustomizer() {
		DefaultOAuth2TokenRevocationCustomizer tokenRevocationCustomizer = new DefaultOAuth2TokenRevocationCustomizer();
		tokenRevocationCustomizer.revocationResponseHandler(OAuth2TokenRevocationAuthenticationSuccessHandler::new);
		return tokenRevocationCustomizer;
	}

	/**
	 * AuthorizationServerJwt
	 */
	@Configuration
	static class AuthorizationServerJwtConfig {

		@Bean
		public JWKSource<SecurityContext> jwkSource() {
			RSAKey rsaKey = Jwks.generateRsa();
			JWKSet jwkSet = new JWKSet(rsaKey);
			return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
		}

	}

}
