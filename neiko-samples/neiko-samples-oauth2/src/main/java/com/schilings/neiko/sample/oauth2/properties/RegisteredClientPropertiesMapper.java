package com.schilings.neiko.sample.oauth2.properties;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames.Client.*;
import static org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames.Token.*;

@Getter
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RegisteredClientProperties.class)
@PropertySource("file:E:/Code/neiko-all-version/1.0.0/neiko/database/oauth2/oauth2-registered-client.properties")
public class RegisteredClientPropertiesMapper implements SmartInitializingSingleton {

	private final List<RegisteredClient> registeredClients = new ArrayList<>();

	private final ObjectMapper objectMapper;

	private final RegisteredClientProperties properties;

	private final RegisteredClientRepository registeredClientRepository;

	public RegisteredClientPropertiesMapper(RegisteredClientProperties properties,
			RegisteredClientRepository registeredClientRepository, Jackson2ObjectMapperBuilder objectMapperBuilder) {

		this.properties = properties;
		this.registeredClientRepository = registeredClientRepository;
		ArrayList<Module> modules = new ArrayList<>();
		// Spring Authorization
		// Server,这个里面有限制SecurityJackson2Modules.enableDefaultTyping(context.getOwner());
		modules.add(new OAuth2AuthorizationServerJackson2Module());
		modules.addAll(SecurityJackson2Modules.getModules(getClass().getClassLoader()));
		objectMapperBuilder.modules(modules::addAll);
		objectMapperBuilder.modules(modules);
		this.objectMapper = objectMapperBuilder.build();
	}

	@Override
	public void afterSingletonsInstantiated() {
		init();
		save();
	}

	public void init() {
		if (!CollectionUtils.isEmpty(properties.getClient())) {
			for (Map<String, String> map : properties.getClient()) {
				registeredClients.add(mapToClient(map));
			}
		}
	}

	public void save() {
		this.registeredClients.forEach(registeredClientRepository::save);
	}

	public RegisteredClient mapToClient(Map<String, String> map) {
		String clientIdIssuedAt = map.get("clientIdIssuedAt");
		String clientSecretExpiresAt = map.get("clientSecretExpiresAt");
		Set<String> clientAuthenticationMethods = StringUtils
				.commaDelimitedListToSet(map.get("clientAuthenticationMethods"));
		Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(map.get("authorizationGrantTypes"));
		Set<String> redirectUris = StringUtils.commaDelimitedListToSet(map.get("redirectUris"));
		Set<String> clientScopes = StringUtils.commaDelimitedListToSet(map.get("scopes"));

		// @formatter:off
		RegisteredClient.Builder builder = RegisteredClient.withId(map.get("id"))
				.clientId(map.get("clientId"))
				.clientIdIssuedAt(clientIdIssuedAt != null ? Instant.parse(clientIdIssuedAt) : null)
				.clientSecret(map.get("clientSecret"))
				.clientSecretExpiresAt(clientSecretExpiresAt != null ? Instant.parse(clientSecretExpiresAt) : null)
				.clientName(map.get("clientName"))
				.clientAuthenticationMethods((authenticationMethods) ->
						clientAuthenticationMethods.forEach(authenticationMethod ->
								authenticationMethods.add(resolveClientAuthenticationMethod(authenticationMethod))))
				.authorizationGrantTypes((grantTypes) ->
						authorizationGrantTypes.forEach(grantType ->
								grantTypes.add(resolveAuthorizationGrantType(grantType))))
				.redirectUris((uris) -> uris.addAll(redirectUris))
				.scopes((scopes) -> scopes.addAll(clientScopes));
		// @formatter:on

		String clientSettings = map.get("clientSettings");
		if (StringUtils.hasText(clientSettings)) {
			builder.clientSettings(toClientSettings(parseMap(map.get("clientSettings"))));
		}
		String tokenSettings = map.get("tokenSettings");
		if (StringUtils.hasText(tokenSettings)) {
			builder.tokenSettings(toTokenSettings(parseMap(tokenSettings)));
		}
		return builder.build();
	}

	protected final ObjectMapper getObjectMapper() {
		return this.objectMapper;
	}

	private Map<String, Object> parseMap(String data) {
		try {
			return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {
			});
		}
		catch (Exception ex) {
			throw new IllegalArgumentException(ex.getMessage(), ex);
		}
	}

	private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
		if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
			return AuthorizationGrantType.AUTHORIZATION_CODE;
		}
		else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
			return AuthorizationGrantType.CLIENT_CREDENTIALS;
		}
		else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
			return AuthorizationGrantType.REFRESH_TOKEN;
		}
		return new AuthorizationGrantType(authorizationGrantType); // Custom authorization
		// grant type
	}

	private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
		if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
			return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
		}
		else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
			return ClientAuthenticationMethod.CLIENT_SECRET_POST;
		}
		else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
			return ClientAuthenticationMethod.NONE;
		}
		return new ClientAuthenticationMethod(clientAuthenticationMethod);
	}

	public static TokenSettings toTokenSettings(Map<String, Object> tokenSettingsMap) {
		if (CollectionUtils.isEmpty(tokenSettingsMap)) {
			return TokenSettings.builder().build();
		}
		return TokenSettings.builder()
				.accessTokenTimeToLive((Duration) Optional.ofNullable(tokenSettingsMap.get(ACCESS_TOKEN_TIME_TO_LIVE))
						.orElse(Duration.ofMinutes(5)))
				.authorizationCodeTimeToLive(
						(Duration) Optional.ofNullable(tokenSettingsMap.get(AUTHORIZATION_CODE_TIME_TO_LIVE))
								.orElse(Duration.ofMinutes(5)))
				.accessTokenFormat((OAuth2TokenFormat) Optional.ofNullable(tokenSettingsMap.get(ACCESS_TOKEN_FORMAT))
						.orElse(OAuth2TokenFormat.SELF_CONTAINED))
				.reuseRefreshTokens(Optional.ofNullable(tokenSettingsMap.get(REUSE_REFRESH_TOKENS))
						.map(b -> (boolean) b).orElse(true))
				.refreshTokenTimeToLive((Duration) Optional.ofNullable(tokenSettingsMap.get(REFRESH_TOKEN_TIME_TO_LIVE))
						.orElse(Duration.ofMinutes(60)))
				.idTokenSignatureAlgorithm(
						(SignatureAlgorithm) Optional.ofNullable(tokenSettingsMap.get(ID_TOKEN_SIGNATURE_ALGORITHM))
								.orElse(SignatureAlgorithm.RS256))
				.build();
	}

	public static ClientSettings toClientSettings(Map<String, Object> clientSettingsMap) {
		if (CollectionUtils.isEmpty(clientSettingsMap)) {
			return ClientSettings.builder().build();
		}
		ClientSettings.Builder builder = ClientSettings.builder()
				.requireProofKey(Optional.ofNullable(clientSettingsMap.get(REQUIRE_PROOF_KEY)).map(b -> (boolean) b)
						.orElse(false))
				.requireAuthorizationConsent(Optional.ofNullable(clientSettingsMap.get(REQUIRE_AUTHORIZATION_CONSENT))
						.map(b -> (boolean) b).orElse(false));
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm
				.from((String) clientSettingsMap.get(TOKEN_ENDPOINT_AUTHENTICATION_SIGNING_ALGORITHM));
		JwsAlgorithm jwsAlgorithm = signatureAlgorithm == null
				? MacAlgorithm.from((String) clientSettingsMap.get(TOKEN_ENDPOINT_AUTHENTICATION_SIGNING_ALGORITHM))
				: signatureAlgorithm;
		if (jwsAlgorithm != null) {
			builder.tokenEndpointAuthenticationSigningAlgorithm(jwsAlgorithm);
		}
		if (StringUtils.hasText((String) clientSettingsMap.get(JWK_SET_URL))) {
			builder.jwkSetUrl((String) clientSettingsMap.get(JWK_SET_URL));
		}
		return builder.build();
	}

}
