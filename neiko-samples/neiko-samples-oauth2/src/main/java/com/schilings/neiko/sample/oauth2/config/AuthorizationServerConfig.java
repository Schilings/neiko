package com.schilings.neiko.sample.oauth2.config;


import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.schilings.neiko.sample.oauth2.jose.Jwks;
import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.config.EnableAuthorizationServer;


import com.schilings.neiko.security.oauth2.authorization.server.configurer.FormLoginRememberMeConfigurer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.oidc.DefaultOAuth2OidcConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityAuthenticationConverter;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.UUID;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig {

	private final UserDetailsService userDetailsService;
	
	@Bean
	public OAuth2AuthorizationServerConfigurerCustomizer authorizationServerConfigurerCustomizer() {
		return (configurer, http) -> {
			//  ExceptionTranslationFilter
			//  FilterSecurityInterceptor
			// 例如授权码模式请求时，未登录，跳转到 资源服务器的SecurityFilterChain
			//http.authorizeRequests().anyRequest().authenticated();
			//http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
			
			//Form Login
			http.apply(new FormLoginRememberMeConfigurer(userDetailsService));
			
			// Custom Authorization Consent
			configurer.authorizationEndpoint(authorizationEndpoint -> {
				authorizationEndpoint.consentPage("/oauth2/consent");
			});
		};
	}

	// Enable OpenID Connect 1.0
	@Bean
	public DefaultOAuth2OidcConfigurerCustomizer oidcConfigurerCustomizer() {
		return new DefaultOAuth2OidcConfigurerCustomizer();
	}
	
	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		String registeredClientId1 = UUID.randomUUID().toString();
		String registeredClientId2 = UUID.randomUUID().toString();

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
				.authorizationGrantType(OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY)
				.redirectUri("http://localhost:9000/oauth2Login")
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
				.authorizationGrantType(OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY)
				.redirectUri("http://localhost:9000/oauth2Login")
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

		// Save registered client in db as if in-memory
		InMemoryRegisteredClientRepository registeredClientRepository = new InMemoryRegisteredClientRepository(
				registeredClient1, registeredClient2);

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
