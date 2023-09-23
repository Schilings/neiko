package com.schilings.neiko.authorization.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.password.OAuth2ResourceOwnerPasswordAuthenticationToken;
import com.schilings.neiko.security.oauth2.authorization.server.jackson.OAuth2AuthorizationMixin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;

import java.security.Principal;
import java.util.*;

public class JacksonTest {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private static final OAuth2AccessTokenGenerator tokenGenerator = new OAuth2AccessTokenGenerator();

	private static RegisteredClient registeredClient;

	private static OAuth2Authorization authorization;

	@BeforeAll
	public static void init() {
		List<Module> securityModules = SecurityJackson2Modules.getModules(JacksonTest.class.getClassLoader());
		objectMapper.registerModules(securityModules);
		objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
		objectMapper.registerModule(new CustomJackson2Module());

		registeredClient = RegisteredClient.withId(UUID.randomUUID().toString()).clientId("messaging-client2")
				.clientSecret("{noop}secret2").clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
				.redirectUri("http://127.0.0.1:8000/login/oauth2/code/sas-oidc-login")
				.redirectUri("http://127.0.0.1:8000/login/oauth2/code/sas-authorization-code-login")
				.redirectUri("http://127.0.0.1:8000/authorized").scope(OidcScopes.OPENID) // 'openid',服务端要开启OIDC
				.scope(OidcScopes.EMAIL) // 针对OIDC请求'openid'的作用域
				.scope(OidcScopes.PHONE).scope(OidcScopes.ADDRESS).scope(OidcScopes.PROFILE).scope("message.read")
				.scope("message.write")
				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
				// 走OpaqueToken的AccessToken
				.tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE).build()).build();

		UserDetails user = User.withUsername("user").password("password").roles("USER").authorities("sys:read")
				.disabled(false).accountLocked(false).accountExpired(false).build();

		HashSet<String> scopes = new HashSet<>(Arrays.asList("openid", "email", "phone", "profile"));

		UsernamePasswordAuthenticationToken usernamePasswordAuthentication = UsernamePasswordAuthenticationToken
				.authenticated(user, user, user.getAuthorities());

		OAuth2ResourceOwnerPasswordAuthenticationToken resourceOwnerPasswordAuthenticationToken = new OAuth2ResourceOwnerPasswordAuthenticationToken(
				"user", usernamePasswordAuthentication, scopes, null);

		DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
				.registeredClient(registeredClient).principal(usernamePasswordAuthentication).authorizedScopes(scopes)
				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
				.authorizationGrant(resourceOwnerPasswordAuthenticationToken);

		OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
				.id(UUID.randomUUID().toString()).principalName(usernamePasswordAuthentication.getName())
				.authorizationGrantType(AuthorizationGrantType.PASSWORD).authorizedScopes(scopes)
				.attribute(Principal.class.getName(), usernamePasswordAuthentication)
				.attribute(UserDetails.class.getName(), usernamePasswordAuthentication.getPrincipal());

		// ----- Access token -----
		OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
		OAuth2Token generatedAccessToken = tokenGenerator.generate(tokenContext);
		OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
				generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
				generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
		authorizationBuilder.accessToken(accessToken);

		authorization = authorizationBuilder.build();

	}

	@Test
	public void test() throws JsonProcessingException {
		String s = objectMapper.writeValueAsString(authorization);

		System.out.println(1);

	}

	public static class CustomJackson2Module extends SimpleModule {

		@Override
		public void setupModule(SetupContext context) {
			context.setMixInAnnotations(OAuth2Authorization.class, OAuth2AuthorizationMixin.class);

		}

	}

}
