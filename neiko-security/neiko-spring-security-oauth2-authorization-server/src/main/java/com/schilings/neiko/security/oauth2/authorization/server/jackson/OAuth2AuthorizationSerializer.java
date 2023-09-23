package com.schilings.neiko.security.oauth2.authorization.server.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class OAuth2AuthorizationSerializer extends JsonSerializer<OAuth2Authorization> {

	@Override
	public void serialize(OAuth2Authorization authorization, JsonGenerator gen, SerializerProvider serializers)
			throws IOException {
		gen.writeStartObject();

		gen.writeStringField("@class", OAuth2Authorization.class.getName());
		gen.writeStringField("id", authorization.getId());
		gen.writeStringField("registeredClientId", authorization.getRegisteredClientId());
		gen.writeStringField("principalName", authorization.getPrincipalName());
		gen.writeStringField("authorizationGrantType", authorization.getAuthorizationGrantType().getValue());
		gen.writeStringField("authorizedScopes",
				StringUtils.collectionToDelimitedString(authorization.getAuthorizedScopes(), ","));
		gen.writeObjectField("attributes", authorization.getAttributes());

		String state = null;
		String authorizationState = authorization.getAttribute(OAuth2ParameterNames.STATE);
		if (StringUtils.hasText(authorizationState)) {
			state = authorizationState;
		}
		gen.writeStringField("state", state);

		OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
				.getToken(OAuth2AuthorizationCode.class);
		if (authorizationCode != null) {
			gen.writeStringField("authorizationCodeValue", authorizationCode.getToken().getTokenValue());
			if (authorizationCode.getToken().getIssuedAt() != null) {
				gen.writeStringField("authorizationCodeIssuedAt",
						authorizationCode.getToken().getIssuedAt().toString());
			}
			if (authorizationCode.getToken().getExpiresAt() != null) {
				gen.writeStringField("authorizationCodeExpiresAt",
						authorizationCode.getToken().getExpiresAt().toString());
			}
			gen.writeObjectField("authorizationCodeMetadata", authorizationCode.getMetadata());
		}

		OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getToken(OAuth2AccessToken.class);
		if (accessToken != null) {
			gen.writeStringField("accessTokenValue", accessToken.getToken().getTokenValue());
			if (accessToken.getToken().getIssuedAt() != null) {
				gen.writeStringField("accessTokenIssuedAt", accessToken.getToken().getIssuedAt().toString());
			}
			if (accessToken.getToken().getExpiresAt() != null) {
				gen.writeStringField("accessTokenExpiresAt", accessToken.getToken().getExpiresAt().toString());
			}
			gen.writeObjectField("accessTokenMetadata", accessToken.getMetadata());

			String accessTokenType = accessToken.getToken().getTokenType().getValue();
			;
			gen.writeStringField("accessTokenType", accessTokenType);

			String accessTokenScopes = null;
			if (!CollectionUtils.isEmpty(accessToken.getToken().getScopes())) {
				accessTokenScopes = StringUtils.collectionToDelimitedString(accessToken.getToken().getScopes(), ",");
				gen.writeStringField("accessTokenScopes", accessTokenScopes);
			}
		}

		OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization.getToken(OidcIdToken.class);
		if (oidcIdToken != null) {
			gen.writeStringField("oidcIdTokenValue", oidcIdToken.getToken().getTokenValue());
			if (oidcIdToken.getToken().getIssuedAt() != null) {
				gen.writeStringField("oidcIdTokenIssuedAt", oidcIdToken.getToken().getIssuedAt().toString());
			}
			if (oidcIdToken.getToken().getExpiresAt() != null) {
				gen.writeStringField("oidcIdTokenExpiresAt", oidcIdToken.getToken().getExpiresAt().toString());
			}
			gen.writeObjectField("oidcIdTokenMetadata", oidcIdToken.getMetadata());
		}

		OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
		if (refreshToken != null) {
			gen.writeStringField("refreshTokenValue", refreshToken.getToken().getTokenValue());
			if (refreshToken.getToken().getIssuedAt() != null) {
				gen.writeStringField("refreshTokenIssuedAt", refreshToken.getToken().getIssuedAt().toString());
			}
			if (refreshToken.getToken().getExpiresAt() != null) {
				gen.writeStringField("refreshTokenExpiresAt", refreshToken.getToken().getExpiresAt().toString());
			}
			gen.writeObjectField("refreshTokenMetadata", refreshToken.getMetadata());
		}

		gen.writeEndObject();
	}

	@Override
	public void serializeWithType(OAuth2Authorization value, JsonGenerator gen, SerializerProvider serializers,
			TypeSerializer typeSer) throws IOException {
		serialize(value, gen, serializers);
	}

}
