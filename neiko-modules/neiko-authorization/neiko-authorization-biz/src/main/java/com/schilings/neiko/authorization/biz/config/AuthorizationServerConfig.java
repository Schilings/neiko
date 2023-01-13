package com.schilings.neiko.authorization.biz.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.schilings.neiko.authorization.biz.handler.OAuth2AccessTokenAuthenticationSuccessHandler;
import com.schilings.neiko.authorization.biz.handler.OAuth2TokenRevocationAuthenticationSuccessHandler;
import com.schilings.neiko.authorization.biz.jose.Jwks;
import com.schilings.neiko.authorization.common.properties.SecurityProperties;
import com.schilings.neiko.oauth2.core.ScopeNames;
import com.schilings.neiko.security.oauth2.authorization.server.config.EnableAuthorizationServer;
import com.schilings.neiko.security.oauth2.authorization.server.configurer.OAuth2LoginPasswordDecoderConfigurer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.authorization.DefaultOAuth2AuthorizationEndpointCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.revocation.DefaultOAuth2TokenRevocationCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.OAuth2TokenEndpointExtensionGrantTypeCustomizer;
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
	@ConditionalOnProperty(name = "neiko.security.password-secret-key", matchIfMissing = false)
	public OAuth2LoginPasswordDecoderConfigurer loginPasswordDecoderConfigurer(SecurityProperties securityProperties) {
		return new OAuth2LoginPasswordDecoderConfigurer(securityProperties.getPasswordSecretKey());
	}

	@Bean
	@ConditionalOnMissingBean
	public DefaultOAuth2AuthorizationEndpointCustomizer authorizationEndpointCustomizer() {
		DefaultOAuth2AuthorizationEndpointCustomizer customizer = new DefaultOAuth2AuthorizationEndpointCustomizer();
		customizer.consentPage("/oauth2/consent");
		return customizer;
	}
	

	@Bean
	@ConditionalOnMissingBean
	public OAuth2TokenEndpointExtensionGrantTypeCustomizer extensionGrantTypeCustomizer() {
		OAuth2TokenEndpointExtensionGrantTypeCustomizer extensionGrantTypeCustomizer = new OAuth2TokenEndpointExtensionGrantTypeCustomizer();
		extensionGrantTypeCustomizer.converterExpander(((converters, http) -> {
			converters.add(new OAuth2ResourceOwnerPasswordAuthenticationConverter());
		}));
		extensionGrantTypeCustomizer.providerExpander((providers, authorizationService, tokenGenerator, http) -> {
			AuthenticationManager authenticationManager = authentication -> http.getSharedObject(AuthenticationManager.class).authenticate(authentication);
			providers.add(new OAuth2ResourceOwnerPasswordAuthenticationProvider(authenticationManager, authorizationService, tokenGenerator));
		});
		extensionGrantTypeCustomizer.accessTokenResponseHandler(OAuth2AccessTokenAuthenticationSuccessHandler::new);
		return extensionGrantTypeCustomizer;
	}


	@Bean
	@ConditionalOnMissingBean
	public DefaultOAuth2TokenRevocationCustomizer tokenRevocationCustomizer() {
		DefaultOAuth2TokenRevocationCustomizer tokenRevocationCustomizer = new DefaultOAuth2TokenRevocationCustomizer();
		tokenRevocationCustomizer.revocationResponseHandler(OAuth2TokenRevocationAuthenticationSuccessHandler::new);
		return tokenRevocationCustomizer;
	}

	
	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		String registeredClientId = UUID.randomUUID().toString();
		RegisteredClient registeredClient = RegisteredClient.withId(registeredClientId).clientId("test")
				.clientSecret("{noop}test")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
				.authorizationGrantType(OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY)
				.redirectUri("http://localhost:9000/oauth2Login")
				.redirectUri("http://127.0.0.1:8000/login/oauth2/code/sas-oidc-login")
				.redirectUri("http://127.0.0.1:8000/login/oauth2/code/sas-authorization-code-login")
				.redirectUri("http://127.0.0.1:8000/authorized").scope(OidcScopes.OPENID) // 'openid',服务端要开启OIDC
				.scope(OidcScopes.EMAIL) // 针对OIDC请求的作用域,携带"openid"说明这是个OIDC请求，如果还携带这些scope说明要获取UserInfo之类
				.scope(OidcScopes.PHONE).scope(OidcScopes.ADDRESS).scope(OidcScopes.PROFILE)
				.scope(ScopeNames.SKIP_PASSWORD_DECODE)//测试客户端跳过密码加解密步骤
				.scope("message.read")
				.scope("message.write")
				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
				.build();

	
		// Save registered client in db as if in-memory
		InMemoryRegisteredClientRepository registeredClientRepository = new InMemoryRegisteredClientRepository(
				registeredClient);

		return registeredClientRepository;
	}


	@Bean
	public OAuth2AuthorizationService authorizationService() {
		return new InMemoryOAuth2AuthorizationService();
	}

	@Bean
	public OAuth2AuthorizationConsentService authorizationConsentService() {
		return new InMemoryOAuth2AuthorizationConsentService();
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
