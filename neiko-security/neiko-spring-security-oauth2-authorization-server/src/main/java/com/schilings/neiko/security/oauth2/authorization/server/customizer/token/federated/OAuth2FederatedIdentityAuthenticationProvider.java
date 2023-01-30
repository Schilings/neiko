package com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated;

import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.OAuth2ExtensionGrantTypeAuthenticationProvider;
import com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2AuthenticationProviderUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.security.Principal;
import java.util.*;

import static com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient;

/**
 *
 * <p>
 * {@link OAuth2AuthorizationCodeRequestAuthenticationProvider}
 * </p>
 * <p>
 * {@link OAuth2AuthorizationCodeAuthenticationProvider}
 * </p>
 *
 * @author Schilings
 */
public class OAuth2FederatedIdentityAuthenticationProvider implements OAuth2ExtensionGrantTypeAuthenticationProvider {

	private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

	private static final OAuth2TokenType ID_TOKEN_TOKEN_TYPE = new OAuth2TokenType(OidcParameterNames.ID_TOKEN);

	private final Log logger = LogFactory.getLog(getClass());

	private final OAuth2AuthorizationService authorizationService;

	private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

	/**
	 * Constructs an {@code OAuth2FederatedIdentityAuthenticationProvider} using the
	 * provided parameters.
	 * @param authorizationService the authorization service
	 * @param tokenGenerator the token generator
	 * @since 1.0.0
	 */
	public OAuth2FederatedIdentityAuthenticationProvider(OAuth2AuthorizationService authorizationService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
		Assert.notNull(authorizationService, "authorizationService cannot be null");
		Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
		this.authorizationService = authorizationService;
		this.tokenGenerator = tokenGenerator;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		OAuth2FederatedIdentityAuthenticationToken federatedIdentityAuthenticationToken = (OAuth2FederatedIdentityAuthenticationToken) authentication;

		OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(
				federatedIdentityAuthenticationToken);
		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

		if (this.logger.isTraceEnabled()) {
			this.logger.trace("Retrieved registered client");
		}

		// 校验授权码，通过授权码能拿到OAuth2Authorization，在OAuth2Login会持久化，在这里拿到
		// 通常用InMemoryOAuth2AuthorizationService，这个不是直接通过code值来判断的，那我们得走里面的matchesState的方式
		// new OAuth2TokenType(OAuth2ParameterNames.STATE)
		// OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY_CODE_TOKEN_TYPE
		OAuth2Authorization authorization = this.authorizationService
				.findByToken(federatedIdentityAuthenticationToken.getCode(), new OAuth2TokenType(OAuth2ParameterNames.STATE));
		if (authorization == null) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
		}
		if (this.logger.isTraceEnabled()) {
			this.logger.trace("Retrieved authorization with federated_identity code");
		}

		// 检验与之前的获取code的client_id是否一致
		if (!registeredClient.getClientId()
				.equals(authorization.getAttribute(OAuth2FederatedIdentityConstant.CLIENT_ID))) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
		}

		// 校验授权方式GrantType
		if (!registeredClient.getAuthorizationGrantTypes()
				.contains(OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY)) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
		}

		// 校验Scope
		Set<String> authorizedScopes = Collections.emptySet();
		if (!CollectionUtils.isEmpty(federatedIdentityAuthenticationToken.getScopes())) {
			for (String requestedScope : federatedIdentityAuthenticationToken.getScopes()) {
				// 检查请求的scope是否合法
				if (!registeredClient.getScopes().contains(requestedScope)) {
					throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
				}
			}
			authorizedScopes = new LinkedHashSet<>(federatedIdentityAuthenticationToken.getScopes());
		}

		// 校验授权码FederatedIdentityCode.Token是否还有效
		OAuth2Authorization.Token<OAuth2FederatedIdentityCode> federatedIdentityCodeToken = authorization
				.getToken(OAuth2FederatedIdentityCode.class);
		if (federatedIdentityCodeToken == null) {
			OAuth2FederatedIdentityCode code = authorization.getAttribute(OAuth2FederatedIdentityCode.class.getName());
			federatedIdentityCodeToken = OAuth2Authorization.from(authorization).token(code).build().getToken(OAuth2FederatedIdentityCode.class);
		}
		if (!federatedIdentityCodeToken.isActive()) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
		}

		if (this.logger.isTraceEnabled()) {
			this.logger.trace("Validated token request parameters");
		}

		// 传递过来的OAuth2LoginAuthenticationToken
		Authentication auth2LoginAuthentication = authorization.getAttribute(Principal.class.getName());

		// @formatter:off
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(auth2LoginAuthentication)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(authorizedScopes)
                .authorizationGrantType(OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY)
                .authorizationGrant(federatedIdentityAuthenticationToken);
        // @formatter:on

		// @formatter:off

        //id不变，顶替原来的authorization,把之前的Code带上，后面标记失效
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.from(authorization)
                .principalName(auth2LoginAuthentication.getName())
                //授权成功给予的scope
                .authorizedScopes(authorizedScopes)
                //在这个OAuth2Authorization中把OAuth2LoginAuthenticationToken携带过去
                .attribute(Principal.class.getName(), authorization.getAttribute(Principal.class.getName()))
                .attribute(OAuth2User.class.getName(), authorization.getAttribute(OAuth2User.class.getName()));
        // @formatter:on

		// ----- Access token -----
		OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
		OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
		if (generatedAccessToken == null) {
			OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
					"The token generator failed to generate the access token.", ERROR_URI);
			throw new OAuth2AuthenticationException(error);
		}

		if (this.logger.isTraceEnabled()) {
			this.logger.trace("Generated access token");
		}

		OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
				generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
				generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());

		if (generatedAccessToken instanceof ClaimAccessor) {
			authorizationBuilder.token(accessToken,
					metadata -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
							((ClaimAccessor) generatedAccessToken).getClaims()));
		}
		else {
			authorizationBuilder.accessToken(accessToken);
		}

		// ----- Refresh token -----
		OAuth2RefreshToken refreshToken = null;
		if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
		// Do not issue refresh token to public client
				!clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

			tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
			OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
			if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
				OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
						"The token generator failed to generate the refresh token.", ERROR_URI);
				throw new OAuth2AuthenticationException(error);
			}
			if (this.logger.isTraceEnabled()) {
				this.logger.trace("Generated refresh token");
			}
			refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
			authorizationBuilder.refreshToken(refreshToken);
		}

		// ----- ID token -----
		OidcIdToken idToken;
		if (authorizedScopes.contains(OidcScopes.OPENID)) {
			// @formatter:off
            tokenContext = tokenContextBuilder
                    .tokenType(ID_TOKEN_TOKEN_TYPE)
                    .authorization(authorizationBuilder.build())	// ID token customizer may need access to the access token and/or refresh token
                    .build();
            // @formatter:on
			OAuth2Token generatedIdToken = this.tokenGenerator.generate(tokenContext);
			if (!(generatedIdToken instanceof Jwt)) {
				OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
						"The token generator failed to generate the ID token.", ERROR_URI);
				throw new OAuth2AuthenticationException(error);
			}

			if (logger.isTraceEnabled()) {
				logger.trace("Generated id token");
			}

			idToken = new OidcIdToken(generatedIdToken.getTokenValue(), generatedIdToken.getIssuedAt(),
					generatedIdToken.getExpiresAt(), ((Jwt) generatedIdToken).getClaims());
			authorizationBuilder.token(idToken,
					(metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, idToken.getClaims()));
		}
		else {
			idToken = null;
		}

		authorization = authorizationBuilder.build();
		// 使授权代码无效，因为它只能使用一次
		authorization = OAuth2AuthenticationProviderUtils.invalidate(authorization,
				federatedIdentityCodeToken.getToken());
		// 认证结束，持久化
		this.authorizationService.save(authorization);

		if (logger.isTraceEnabled()) {
			logger.trace("Saved authorization");
		}

		Map<String, Object> additionalParameters = Collections.emptyMap();
		if (idToken != null) {
			additionalParameters = new HashMap<>();
			additionalParameters.put(OidcParameterNames.ID_TOKEN, idToken.getTokenValue());
		}
		logger.debug("OAuth2Authorization saved successfully, then returning OAuth2AccessTokenAuthenticationToken");

		return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken,
				additionalParameters);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return OAuth2FederatedIdentityAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
