package com.schilings.neiko.sample.authorization.server.config;


import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.schilings.neiko.sample.authorization.server.event.DefaultApplicationEventAuthenticationFailureHandler;
import com.schilings.neiko.sample.authorization.server.event.DefaultApplicationEventAuthenticationSuccessHandler;
import com.schilings.neiko.sample.authorization.server.jose.Jwks;
import com.schilings.neiko.sample.authorization.server.properties.RegisteredClientPropertiesMapper;
import com.schilings.neiko.security.oauth2.authorization.server.config.EnableAuthorizationServer;
import com.schilings.neiko.security.oauth2.authorization.server.configurer.FormLoginRememberMeConfigurer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.authorization.DefaultOAuth2AuthorizationEndpointCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.oidc.DefaultOAuth2OidcConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.OAuth2TokenEndpointExtensionGrantTypeCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.oidc.OidcIdTokenCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.password.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.password.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;


import java.util.HashMap;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@EnableAuthorizationServer
public class AuthorizationServerConfig {
	
	
	@Bean
	public FormLoginRememberMeConfigurer formLoginRememberMeConfigurer(UserDetailsService userDetailsService) {
		return new FormLoginRememberMeConfigurer(userDetailsService);
	}


	// Custom Authorization Consent
	@Bean
	public DefaultOAuth2AuthorizationEndpointCustomizer authorizationEndpointCustomizer() {
		DefaultOAuth2AuthorizationEndpointCustomizer customizer = new DefaultOAuth2AuthorizationEndpointCustomizer();
		customizer.consentPage("/oauth2/consent");
		return customizer;
	}
	
	
	// Enable OpenID Connect 1.0
	@Bean
	public DefaultOAuth2OidcConfigurerCustomizer oidcConfigurerCustomizer() {
		DefaultOAuth2OidcConfigurerCustomizer customizer = new DefaultOAuth2OidcConfigurerCustomizer();
		customizer.userInfoMapper(authenticationContext -> {
			OAuth2Authorization authorization = authenticationContext.getAuthorization();
			OidcIdToken idToken = authorization.getToken(OidcIdToken.class).getToken();
			HashMap<String, Object> claims = new HashMap<>(idToken.getClaims());
			claims.put("123333333333", "12333333333");
			//全部返回
			return new OidcUserInfo(claims);
		});
		return customizer;
	}


	// Token Endpoint
	@Bean
	public OAuth2TokenEndpointExtensionGrantTypeCustomizer extensionGrantTypeCustomizer() {
		OAuth2TokenEndpointExtensionGrantTypeCustomizer extensionGrantTypeCustomizer = new OAuth2TokenEndpointExtensionGrantTypeCustomizer();
		// converter
		extensionGrantTypeCustomizer.converterExpander(((converters, http) -> {
			converters.add(new OAuth2ResourceOwnerPasswordAuthenticationConverter());
		}));
		// provider
		extensionGrantTypeCustomizer.providerExpander((providers, authorizationService, tokenGenerator, http) -> {
			// 未build,so 懒加载
			AuthenticationManager authenticationManager = authentication -> http.getSharedObject(AuthenticationManager.class).authenticate(authentication);
			providers.add(new OAuth2ResourceOwnerPasswordAuthenticationProvider(authenticationManager,
					authorizationService, tokenGenerator));
		});
		// handler
		extensionGrantTypeCustomizer.accessTokenResponseHandler(DefaultApplicationEventAuthenticationSuccessHandler::new);
		extensionGrantTypeCustomizer.errorResponseHandler(DefaultApplicationEventAuthenticationFailureHandler::new);
		return extensionGrantTypeCustomizer;
	}
	
	
	
	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder()
				// 默认端点URI
				// /oauth2/token可以通过构造函数OAuth2TokenEndpointFilter(AuthenticationManager,
				// String)覆盖
				.tokenEndpoint("/oauth2/token")
				.oidcUserInfoEndpoint("/oidc/userinfo")
				.build();
	}

	@Bean
	public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate, RegisteredClientPropertiesMapper mapper) {

		List<RegisteredClient> registeredClients = mapper.getRegisteredClients();

		String registeredClientId1 = "registeredClientId1";
		String registeredClientId2 = "registeredClientId2";
		
		RegisteredClient registeredClient1 = RegisteredClient.withId(registeredClientId1)
				.clientId("messaging-client1")
				.clientSecret("{noop}secret1")
				// 这里是CLIENT_SECRET_POST,方便调试，客户端认证，直接在post请求中传client_id和client_secret
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
				.redirectUri("http://127.0.0.1:8000/login/oauth2/code/sas-oidc-login")
				.redirectUri("http://127.0.0.1:8000/login/oauth2/code/sas-authorization-code-login")
				.redirectUri("http://127.0.0.1:8000/authorized")
				.scope(OidcScopes.OPENID) // 'openid',服务端要开启OIDC
				.scope(OidcScopes.EMAIL) // 针对OIDC请求的作用域,携带"openid"说明这是个OIDC请求，如果还携带这些scope说明要获取UserInfo之类
				.scope(OidcScopes.PHONE)
				.scope(OidcScopes.ADDRESS)
				.scope(OidcScopes.PROFILE) 
				.scope("message.read")
				.scope("message.write")
				// 设置需要同意requireAuthorizationConsent(true),这里设置false，方便测试
				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
				//OAuth2AccessTokenGenerator: 只要是OAuth2TokenType.ACCESS_TOKEN 或者配置了OAuth2TokenFormat.REFERENCE 即可
				//							  用于生成默认类型的AccessToken令牌
				//JwtGenerator：只支持OAuth2TokenType.ACCESS_TOKEN和OidcParameterNames.ID_TOKEN这两种令牌类型
				//				而且如果是OAuth2TokenType.ACCESS_TOKEN那么要求客户端配置是OAuth2TokenFormat.SELF_CONTAINED
				//				用于生成JWT类型的AccessToken的令牌
				// (一般不用显式设置)默认build的客户端含OAuth2TokenFormat.SELF_CONTAINED
				//故如果要生成ACCESS_TOKEN，生成access_token需要客户端支持这个OAuth2TokenFormat.REFERENCE
				//.tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE).build())
				//如果不显示设置的话，OAuth2TokenFormat.SELF_CONTAINED生成就是要JwtGenerator，就要进行服务端能支持Jwt令牌的生成
				//配置JwtSource JwtEncoder等等
				.build();


		//不同的客户端 id clientId clienSecret 都不能一样
		RegisteredClient registeredClient2 = RegisteredClient.withId(registeredClientId2)
				.clientId("messaging-client2")
				.clientSecret("{noop}secret2")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
				.redirectUri("http://127.0.0.1:8000/login/oauth2/code/sas-oidc-login")
				.redirectUri("http://127.0.0.1:8000/login/oauth2/code/sas-authorization-code-login")
				.redirectUri("http://127.0.0.1:8000/authorized")
				.scope(OidcScopes.OPENID) // 'openid',服务端要开启OIDC
				.scope(OidcScopes.EMAIL) // 针对OIDC请求'openid'的作用域
				.scope(OidcScopes.PHONE)
				.scope(OidcScopes.ADDRESS)
				.scope(OidcScopes.PROFILE)
				.scope("message.read")
				.scope("message.write")
				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
				//走OpaqueToken的AccessToken
				.tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE).build())
				.build();

		registeredClients.add(registeredClient1);
		registeredClients.add(registeredClient2);
		JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
		registeredClients.forEach(registeredClientRepository::save);
		return registeredClientRepository;
	}


	@Bean
	public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
														   RegisteredClientRepository registeredClientRepository) {
		JdbcOAuth2AuthorizationService service = new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
		return service;
		//return new RedisOAuth2AuthorizationService();
	}
	
	@Bean
	public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
																		 RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
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

		@Bean
		public OAuth2TokenCustomizer<JwtEncodingContext> oidcIdTokenCustomizer() {
			return new OidcIdTokenCustomizer();
		}
	}
	
	
}
