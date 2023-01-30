package com.schilings.neiko.samples.admin.security.properties;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RegisteredClientProperties.class)
@PropertySource("file:E:/Code/neiko-all-version/1.0.0/neiko/database/oauth2/oauth2-registered-client.properties")
public class RegisteredClientPropertiesMapper implements SmartInitializingSingleton {

	private final List<RegisteredClient> registeredClients = new ArrayList<>();

	private ObjectMapper objectMapper = new ObjectMapper();

	private final RegisteredClientProperties properties;

	private final RegisteredClientRepository registeredClientRepository;

	public RegisteredClientPropertiesMapper(RegisteredClientProperties properties,
											RegisteredClientRepository registeredClientRepository) {
		this.properties = properties;
		this.registeredClientRepository = registeredClientRepository;
		ClassLoader classLoader = RegisteredClientPropertiesMapper.class.getClassLoader();
		List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
		this.objectMapper.registerModules(securityModules);
		this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
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
			builder.clientSettings(ClientSettings.withSettings(parseMap(clientSettings)).build());
		} else {
			builder.clientSettings(ClientSettings.builder().build());
		}

		String tokenSettings = map.get("tokenSettings");
		if (StringUtils.hasText(tokenSettings)) {
			Map<String, Object> tokenSettingsMap = parseMap(tokenSettings);
			TokenSettings.Builder tokenSettingsBuilder = TokenSettings.withSettings(tokenSettingsMap);
			if (!tokenSettingsMap.containsKey(ConfigurationSettingNames.Token.ACCESS_TOKEN_FORMAT)) {
				tokenSettingsBuilder.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED);
			}
			builder.tokenSettings(tokenSettingsBuilder.build());
		} else {
			builder.tokenSettings(TokenSettings.builder().build());
		}
		return builder.build();
	}


	public final void setObjectMapper(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "objectMapper cannot be null");
		this.objectMapper = objectMapper;
	}

	protected final ObjectMapper getObjectMapper() {
		return this.objectMapper;
	}

	private Map<String, Object> parseMap(String data) {
		try {
			return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex.getMessage(), ex);
		}
	}

	private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
		if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
			return AuthorizationGrantType.AUTHORIZATION_CODE;
		} else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
			return AuthorizationGrantType.CLIENT_CREDENTIALS;
		} else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
			return AuthorizationGrantType.REFRESH_TOKEN;
		}
		return new AuthorizationGrantType(authorizationGrantType); // Custom authorization
		// grant type
	}

	private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
		if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
			return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
		} else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
			return ClientAuthenticationMethod.CLIENT_SECRET_POST;
		} else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
			return ClientAuthenticationMethod.NONE;
		}
		return new ClientAuthenticationMethod(clientAuthenticationMethod); // Custom
		// client
		// authentication
		// method
	}


}