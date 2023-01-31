package com.schilings.neiko.sample.authorization.server.config;


import com.fasterxml.jackson.databind.Module;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.schilings.neiko.sample.authorization.server.event.DefaultApplicationEventAuthenticationFailureHandler;
import com.schilings.neiko.sample.authorization.server.event.DefaultApplicationEventAuthenticationSuccessHandler;
import com.schilings.neiko.sample.authorization.server.jose.Jwks;
import com.schilings.neiko.security.oauth2.authorization.server.autoconfigure.EnableAuthorizationServer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.oidc.DefaultOAuth2OidcConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.DefaultOAuth2TokenEndpointConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.jackson.AuthorizationServerJackson2Module;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@EnableAuthorizationServer
public class AuthorizationServerConfig {
	
	
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
	public DefaultOAuth2TokenEndpointConfigurerCustomizer extensionGrantTypeCustomizer() {
		DefaultOAuth2TokenEndpointConfigurerCustomizer extensionGrantTypeCustomizer = new DefaultOAuth2TokenEndpointConfigurerCustomizer();
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
	public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
		List<RegisteredClient> registeredClients = new ArrayList<>();
//		String registeredClientId1 = "registeredClientId1";
//		String registeredClientId2 = "registeredClientId2";
//		RegisteredClient registeredClient1 = RegisteredClient.withId(registeredClientId1)
//				.clientId("messaging-client1")
//				.clientSecret("{noop}secret1")
//				// 这里是CLIENT_SECRET_POST,方便调试，客户端认证，直接在post请求中传client_id和client_secret
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
//				.redirectUri("http://127.0.0.1:8000/login/oauth2/code/sas-oidc-login")
//				.redirectUri("http://127.0.0.1:8000/login/oauth2/code/sas-authorization-code-login")
//				.redirectUri("http://127.0.0.1:8000/authorized")
//				.scope(OidcScopes.OPENID) // 'openid',服务端要开启OIDC
//				.scope(OidcScopes.EMAIL) // 针对OIDC请求的作用域,携带"openid"说明这是个OIDC请求，如果还携带这些scope说明要获取UserInfo之类
//				.scope(OidcScopes.PHONE)
//				.scope(OidcScopes.ADDRESS)
//				.scope(OidcScopes.PROFILE) 
//				.scope("message.read")
//				.scope("message.write")
//				// 设置需要同意requireAuthorizationConsent(true),这里设置false，方便测试
//				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
//				//OAuth2AccessTokenGenerator: 只要是OAuth2TokenType.ACCESS_TOKEN 或者配置了OAuth2TokenFormat.REFERENCE 即可
//				//							  用于生成默认类型的AccessToken令牌
//				//JwtGenerator：只支持OAuth2TokenType.ACCESS_TOKEN和OidcParameterNames.ID_TOKEN这两种令牌类型
//				//				而且如果是OAuth2TokenType.ACCESS_TOKEN那么要求客户端配置是OAuth2TokenFormat.SELF_CONTAINED
//				//				用于生成JWT类型的AccessToken的令牌
//				// (一般不用显式设置)默认build的客户端含OAuth2TokenFormat.SELF_CONTAINED
//				//故如果要生成ACCESS_TOKEN，生成access_token需要客户端支持这个OAuth2TokenFormat.REFERENCE
//				//.tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE).build())
//				//如果不显示设置的话，OAuth2TokenFormat.SELF_CONTAINED生成就是要JwtGenerator，就要进行服务端能支持Jwt令牌的生成
//				//配置JwtSource JwtEncoder等等
//				.build();
//
//
//		//不同的客户端 id clientId clienSecret 都不能一样
//		RegisteredClient registeredClient2 = RegisteredClient.withId(registeredClientId2)
//				.clientId("messaging-client2")
//				.clientSecret("{noop}secret2")
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
//				.redirectUri("http://127.0.0.1:8000/login/oauth2/code/sas-oidc-login")
//				.redirectUri("http://127.0.0.1:8000/login/oauth2/code/sas-authorization-code-login")
//				.redirectUri("http://127.0.0.1:8000/authorized")
//				.scope(OidcScopes.OPENID) // 'openid',服务端要开启OIDC
//				.scope(OidcScopes.EMAIL) // 针对OIDC请求'openid'的作用域
//				.scope(OidcScopes.PHONE)
//				.scope(OidcScopes.ADDRESS)
//				.scope(OidcScopes.PROFILE)
//				.scope("message.read")
//				.scope("message.write")
//				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
//				//走OpaqueToken的AccessToken
//				.tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE).build())
//				.build();
//		registeredClients.add(registeredClient1);
//		registeredClients.add(registeredClient2);
		
		JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
		registeredClients.forEach(registeredClientRepository::save);
		return registeredClientRepository;
	}

	@Bean
	public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
														   RegisteredClientRepository registeredClientRepository,
														   //Jackson2ObjectMapperBuilder是Scope("prototype")
														   Jackson2ObjectMapperBuilder objectMapperBuilder) {
		ClassLoader classLoader = JdbcRegisteredClientRepository.class.getClassLoader();
		ArrayList<Module> modules = new ArrayList<>();
		//Spring Authorization Server,这个里面有限制SecurityJackson2Modules.enableDefaultTyping(context.getOwner());
		modules.add(new OAuth2AuthorizationServerJackson2Module());
		//Neiko Authorization Server
		modules.add(new AuthorizationServerJackson2Module());
		//Spring Security
		modules.addAll(SecurityJackson2Modules.getModules(classLoader));
		objectMapperBuilder.modules(modules::addAll);
		objectMapperBuilder.modules(modules);
		
		JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper authorizationRowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(registeredClientRepository);
		authorizationRowMapper.setLobHandler(new DefaultLobHandler());
		authorizationRowMapper.setObjectMapper(objectMapperBuilder.build());
		JdbcOAuth2AuthorizationService authorizationService = new JdbcOAuth2AuthorizationService(jdbcTemplate,
				registeredClientRepository);
		authorizationService.setAuthorizationRowMapper(authorizationRowMapper);
		return authorizationService;
		
		
		//return new RedisOAuth2AuthorizationService(objectMapperBuilder.build());
		
	}
	
	
	@Bean
	public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
																		 RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
	}
	

	/**
	 * AuthorizationServerJwt
	 */
	@Configuration(proxyBeanMethods = false)
	static class AuthorizationServerJwtConfig {
		
		@Bean
		@ConditionalOnMissingBean
		public JWKSource<SecurityContext> jwkSource() {
			RSAKey rsaKey = Jwks.generateRsa();
			JWKSet jwkSet = new JWKSet(rsaKey);
			return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
		}
		
	}
	
	
}
