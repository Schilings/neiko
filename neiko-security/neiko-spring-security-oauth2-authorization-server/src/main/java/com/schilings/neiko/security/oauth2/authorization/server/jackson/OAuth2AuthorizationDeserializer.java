package com.schilings.neiko.security.oauth2.authorization.server.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2AuthorizationServerComponentUtils;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class OAuth2AuthorizationDeserializer extends JsonDeserializer<OAuth2Authorization> {

	@Override
	public OAuth2Authorization deserialize(JsonParser parser, DeserializationContext ctxt)
			throws IOException, JacksonException {
		ObjectMapper mapper = (ObjectMapper) parser.getCodec();
		JsonNode root = mapper.readTree(parser);
		return deserialize(parser, mapper, root);
	}

	private OAuth2Authorization deserialize(JsonParser parser, ObjectMapper mapper, JsonNode root)
			throws JsonParseException {

		RegisteredClient client = OAuth2AuthorizationServerComponentUtils.getRegisteredClientRepository()
				.findById(JsonNodeUtils.findStringValue(root, "registeredClientId"));
		OAuth2Authorization.Builder builder = getBuilder(parser, client);
		String id = JsonNodeUtils.findStringValue(root, "id");
		String principalName = JsonNodeUtils.findStringValue(root, "principalName");
		String authorizationGrantType = JsonNodeUtils.findStringValue(root, "authorizationGrantType");
		Set<String> authorizedScopes = Collections.emptySet();
		String authorizedScopesString = JsonNodeUtils.findStringValue(root, "authorizedScopes");
		if (authorizedScopesString != null) {
			authorizedScopes = StringUtils.commaDelimitedListToSet(authorizedScopesString);
		}
		Map<String, Object> attributes = JsonNodeUtils.findValue(root, "attributes", JsonNodeUtils.STRING_OBJECT_MAP,
				mapper);

		builder.id(id).principalName(principalName)
				.authorizationGrantType(new AuthorizationGrantType(authorizationGrantType))
				.authorizedScopes(authorizedScopes).attributes((attrs) -> attrs.putAll(attributes));

		String state = JsonNodeUtils.findStringValue(root, "state");
		if (StringUtils.hasText(state)) {
			builder.attribute(OAuth2ParameterNames.STATE, state);
		}

		Instant tokenIssuedAt;
		Instant tokenExpiresAt;
		String authorizationCodeValue = JsonNodeUtils.findStringValue(root, "authorizationCodeValue");

		if (StringUtils.hasText(authorizationCodeValue)) {

			tokenIssuedAt = Instant.parse(JsonNodeUtils.findStringValue(root, "authorizationCodeIssuedAt"));
			tokenExpiresAt = Instant.parse(JsonNodeUtils.findStringValue(root, "authorizationCodeExpiresAt"));
			Map<String, Object> authorizationCodeMetadata = JsonNodeUtils.findValue(root, "authorizationCodeMetadata",
					JsonNodeUtils.STRING_OBJECT_MAP, mapper);

			OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(authorizationCodeValue,
					tokenIssuedAt, tokenExpiresAt);
			builder.token(authorizationCode, (metadata) -> metadata.putAll(authorizationCodeMetadata));
		}

		String accessTokenValue = JsonNodeUtils.findStringValue(root, "accessTokenValue");
		if (StringUtils.hasText(accessTokenValue)) {
			tokenIssuedAt = Instant.parse(JsonNodeUtils.findStringValue(root, "accessTokenIssuedAt"));
			tokenExpiresAt = Instant.parse(JsonNodeUtils.findStringValue(root, "accessTokenExpiresAt"));
			Map<String, Object> accessTokenMetadata = JsonNodeUtils.findValue(root, "accessTokenMetadata",
					JsonNodeUtils.STRING_OBJECT_MAP, mapper);

			OAuth2AccessToken.TokenType tokenType = null;
			if (OAuth2AccessToken.TokenType.BEARER.getValue()
					.equalsIgnoreCase(JsonNodeUtils.findStringValue(root, "accessTokenType"))) {
				tokenType = OAuth2AccessToken.TokenType.BEARER;
			}

			Set<String> scopes = Collections.emptySet();
			String accessTokenScopes = JsonNodeUtils.findStringValue(root, "accessTokenScopes");
			if (accessTokenScopes != null) {
				scopes = StringUtils.commaDelimitedListToSet(accessTokenScopes);
			}
			OAuth2AccessToken accessToken = new OAuth2AccessToken(tokenType, accessTokenValue, tokenIssuedAt,
					tokenExpiresAt, scopes);
			builder.token(accessToken, (metadata) -> metadata.putAll(accessTokenMetadata));
		}

		String oidcIdTokenValue = JsonNodeUtils.findStringValue(root, "oidcIdTokenValue");
		if (StringUtils.hasText(oidcIdTokenValue)) {
			tokenIssuedAt = Instant.parse(JsonNodeUtils.findStringValue(root, "oidcIdTokenIssuedAt"));
			tokenExpiresAt = Instant.parse(JsonNodeUtils.findStringValue(root, "oidcIdTokenExpiresAt"));
			Map<String, Object> oidcTokenMetadata = JsonNodeUtils.findValue(root, "oidcIdTokenMetadata",
					JsonNodeUtils.STRING_OBJECT_MAP, mapper);
			OidcIdToken oidcToken = new OidcIdToken(oidcIdTokenValue, tokenIssuedAt, tokenExpiresAt,
					(Map<String, Object>) oidcTokenMetadata.get(OAuth2Authorization.Token.CLAIMS_METADATA_NAME));
			builder.token(oidcToken, (metadata) -> metadata.putAll(oidcTokenMetadata));
		}

		String refreshTokenValue = JsonNodeUtils.findStringValue(root, "refreshTokenValue");
		if (StringUtils.hasText(refreshTokenValue)) {
			tokenIssuedAt = Instant.parse(JsonNodeUtils.findStringValue(root, "refreshTokenIssuedAt"));
			tokenExpiresAt = null;
			Instant refreshTokenExpiresAt = null;
			if (StringUtils.hasText(JsonNodeUtils.findStringValue(root, "refreshTokenExpiresAt"))) {
				refreshTokenExpiresAt = Instant.parse(JsonNodeUtils.findStringValue(root, "refreshTokenExpiresAt"));
				tokenExpiresAt = refreshTokenExpiresAt;
			}
			Map<String, Object> refreshTokenMetadata = JsonNodeUtils.findValue(root, "refreshTokenMetadata",
					JsonNodeUtils.STRING_OBJECT_MAP, mapper);

			OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(refreshTokenValue, tokenIssuedAt, tokenExpiresAt);
			builder.token(refreshToken, (metadata) -> metadata.putAll(refreshTokenMetadata));
		}

		return builder.build();
	}

	private OAuth2Authorization.Builder getBuilder(JsonParser parser, RegisteredClient registeredClient)
			throws JsonParseException {
		return OAuth2Authorization.withRegisteredClient(registeredClient);
	}

}
