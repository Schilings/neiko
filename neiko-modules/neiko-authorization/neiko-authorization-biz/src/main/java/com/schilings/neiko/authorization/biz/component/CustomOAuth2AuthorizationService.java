package com.schilings.neiko.authorization.biz.component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schilings.neiko.authorization.biz.service.AuthorizationService;
import com.schilings.neiko.authorization.model.entity.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class CustomOAuth2AuthorizationService implements OAuth2AuthorizationService {

	private final AuthorizationService authorizationService;

	private final RegisteredClientRepository registeredClientRepository;

	private final ObjectMapper objectMapper;

	@Override
	public void save(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");
		OAuth2Authorization existingAuthorization = findById(authorization.getId());
		if (existingAuthorization == null) {
			authorizationService.save(toEntity(authorization));
		}
		else {
			authorizationService.updateById(toEntity(authorization));
		}
	}

	@Override
	public void remove(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");
		authorizationService.removeById(authorization.getId());
	}

	@Override
	public OAuth2Authorization findById(String id) {
		Authorization authorization = authorizationService.getById(id);
		return authorization != null ? toObject(authorization) : null;
	}

	@Override
	public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
		Assert.hasText(token, "token cannot be empty");
		Authorization authorization = null;
		if (tokenType == null) {
			authorization = authorizationService.getIfUnkonwTokenType(token, token, token, token);
		}
		else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
			authorization = authorizationService.getByState(token);
		}
		else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
			authorization = authorizationService.getByAuthorizationCode(token);
		}
		else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
			authorization = authorizationService.getByAccessToken(token);
		}
		else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
			authorization = authorizationService.getByRefreshToken(token);
		}
		return authorization != null ? toObject(authorization) : null;
	}

	private OAuth2Authorization toObject(Authorization entity) {
		RegisteredClient registeredClient = this.registeredClientRepository.findById(entity.getRegisteredClientId());
		if (registeredClient == null) {
			throw new DataRetrievalFailureException("The RegisteredClient with id '" + entity.getRegisteredClientId()
					+ "' was not found in the RegisteredClientRepository.");
		}

		OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient)
				.id(String.valueOf(entity.getId())).principalName(entity.getPrincipalName())
				.authorizationGrantType(resolveAuthorizationGrantType(entity.getAuthorizationGrantType()))
				.attributes(attributes -> attributes.putAll(parseMap(entity.getAttributes())));

		if (StringUtils.hasText(entity.getState())) {
			builder.attribute(OAuth2ParameterNames.STATE, entity.getState());
		}

		if (StringUtils.hasText(entity.getAuthorizationCodeValue())) {
			OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(entity.getAuthorizationCodeValue(),
					Instant.parse(entity.getAuthorizationCodeIssuedAt()),
					Instant.parse(entity.getAuthorizationCodeExpiresAt()));
			builder.token(authorizationCode,
					metadata -> metadata.putAll(parseMap(entity.getAuthorizationCodeMetadata())));
		}

		if (StringUtils.hasText(entity.getAccessTokenValue())) {
			OAuth2AccessToken.TokenType tokenType = null;
			if (OAuth2AccessToken.TokenType.BEARER.getValue().equalsIgnoreCase(entity.getAccessTokenType())) {
				tokenType = OAuth2AccessToken.TokenType.BEARER;
			}
			tokenType = OAuth2AccessToken.TokenType.BEARER;
			Set<String> scopes = Collections.emptySet();
			if (StringUtils.hasText(entity.getAccessTokenScopes())) {
				scopes = StringUtils.commaDelimitedListToSet(entity.getAccessTokenScopes());
			}
			builder.authorizedScopes(scopes);
			OAuth2AccessToken accessToken = new OAuth2AccessToken(tokenType, entity.getAccessTokenValue(),
					Instant.parse(entity.getAccessTokenIssuedAt()), Instant.parse(entity.getAccessTokenExpiresAt()),
					scopes);
			builder.token(accessToken, metadata -> metadata.putAll(parseMap(entity.getAccessTokenMetadata())));
		}

		if (StringUtils.hasText(entity.getRefreshTokenValue())) {
			OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(entity.getRefreshTokenValue(),
					Instant.parse(entity.getRefreshTokenIssuedAt()), Instant.parse(entity.getRefreshTokenExpiresAt()));
			builder.token(refreshToken, metadata -> metadata.putAll(parseMap(entity.getRefreshTokenMetadata())));
		}

		if (StringUtils.hasText(entity.getOidcIdTokenValue())) {
			OidcIdToken idToken = new OidcIdToken(entity.getOidcIdTokenValue(),
					Instant.parse(entity.getOidcIdTokenIssuedAt()), Instant.parse(entity.getOidcIdTokenExpiresAt()),
					parseMap(entity.getOidcIdTokenClaims()));
			builder.token(idToken, metadata -> metadata.putAll(parseMap(entity.getOidcIdTokenMetadata())));
		}
		return builder.build();
	}

	private Authorization toEntity(OAuth2Authorization authorization) {
		Authorization entity = new Authorization();
		entity.setId(authorization.getId());
		entity.setRegisteredClientId(authorization.getRegisteredClientId());
		entity.setPrincipalName(authorization.getPrincipalName());
		entity.setAuthorizationGrantType(authorization.getAuthorizationGrantType().getValue());
		entity.setAttributes(writeMap(authorization.getAttributes()));
		entity.setState(authorization.getAttribute(OAuth2ParameterNames.STATE));

		OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
				.getToken(OAuth2AuthorizationCode.class);
		setTokenValues(authorizationCode, entity::setAuthorizationCodeValue, entity::setAuthorizationCodeIssuedAt,
				entity::setAuthorizationCodeExpiresAt, entity::setAuthorizationCodeMetadata);

		OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getToken(OAuth2AccessToken.class);
		setTokenValues(accessToken, entity::setAccessTokenValue, entity::setAccessTokenIssuedAt,
				entity::setAccessTokenExpiresAt, entity::setAccessTokenMetadata);
		if (accessToken != null) {
			if (accessToken.getToken().getScopes() != null) {
				entity.setAccessTokenScopes(
						StringUtils.collectionToDelimitedString(accessToken.getToken().getScopes(), ","));
			}
			if (accessToken.getToken().getTokenType() != null) {
				entity.setAccessTokenType(accessToken.getToken().getTokenType().getValue());
			}
		}

		OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getToken(OAuth2RefreshToken.class);
		setTokenValues(refreshToken, entity::setRefreshTokenValue, entity::setRefreshTokenIssuedAt,
				entity::setRefreshTokenExpiresAt, entity::setRefreshTokenMetadata);

		OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization.getToken(OidcIdToken.class);
		setTokenValues(oidcIdToken, entity::setOidcIdTokenValue, entity::setOidcIdTokenIssuedAt,
				entity::setOidcIdTokenExpiresAt, entity::setOidcIdTokenMetadata);
		if (oidcIdToken != null) {
			entity.setOidcIdTokenClaims(writeMap(oidcIdToken.getClaims()));
		}

		return entity;
	}

	private void setTokenValues(OAuth2Authorization.Token<?> token, Consumer<String> tokenValueConsumer,
			Consumer<String> issuedAtConsumer, Consumer<String> expiresAtConsumer, Consumer<String> metadataConsumer) {
		if (token != null) {
			OAuth2Token oAuth2Token = token.getToken();
			tokenValueConsumer.accept(oAuth2Token.getTokenValue());
			issuedAtConsumer.accept(Optional.ofNullable(oAuth2Token.getIssuedAt()).map(Instant::toString).orElse(null));
			expiresAtConsumer
					.accept(Optional.ofNullable(oAuth2Token.getExpiresAt()).map(Instant::toString).orElse(null));
			metadataConsumer.accept(writeMap(token.getMetadata()));
		}
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

	private String writeMap(Map<String, Object> metadata) {
		try {
			return this.objectMapper.writeValueAsString(metadata);
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
		// Custom authorization grant type
		return new AuthorizationGrantType(authorizationGrantType);
	}

}
