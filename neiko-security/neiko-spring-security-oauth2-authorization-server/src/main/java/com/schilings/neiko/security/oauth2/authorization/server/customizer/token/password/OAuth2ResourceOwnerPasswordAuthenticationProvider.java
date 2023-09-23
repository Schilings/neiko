package com.schilings.neiko.security.oauth2.authorization.server.customizer.token.password;

import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.OAuth2ExtensionGrantTypeAuthenticationProvider;
import com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2AuthenticationProviderUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.*;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.security.Principal;
import java.util.*;

/**
 *
 * <p>
 * 放弃注入容器的打算，会破坏默认配置{@see InitializeAuthenticationProviderManagerConfigurer}
 * </p>
 *
 * @see OAuth2AuthorizationCodeRequestAuthenticationProvider
 * @see OAuth2AuthorizationCodeAuthenticationProvider
 * @see OAuth2ClientCredentialsAuthenticationProvider
 * @author Schilings
 */
public class OAuth2ResourceOwnerPasswordAuthenticationProvider
		implements OAuth2ExtensionGrantTypeAuthenticationProvider {

	private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

	private static final OAuth2TokenType ID_TOKEN_TOKEN_TYPE = new OAuth2TokenType(OidcParameterNames.ID_TOKEN);

	private static final OAuth2TokenType PASSWORD_TOKEN_TYPE = new OAuth2TokenType(OAuth2ParameterNames.PASSWORD);

	private final Log logger = LogFactory.getLog(getClass());

	private final AuthenticationManager authenticationManager;

	private final OAuth2AuthorizationService authorizationService;

	private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

	/**
	 * Constructs an {@code OAuth2ResourceOwnerPasswordAuthenticationProvider} using the
	 * provided parameters.
	 * @param authenticationManager the authentication manager
	 * @param authorizationService the authorization service
	 * @param tokenGenerator the token generator
	 * @since 1.0.0
	 */
	public OAuth2ResourceOwnerPasswordAuthenticationProvider(AuthenticationManager authenticationManager,
			OAuth2AuthorizationService authorizationService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
		Assert.notNull(authorizationService, "authorizationService cannot be null");
		Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
		this.authenticationManager = authenticationManager;
		this.authorizationService = authorizationService;
		this.tokenGenerator = tokenGenerator;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		OAuth2ResourceOwnerPasswordAuthenticationToken resourceOwnerPasswordAuthenticationToken = (OAuth2ResourceOwnerPasswordAuthenticationToken) authentication;

		// 已经认证的经过OAuth2ClientAuthenticationFilter认证后的OAuth2ClientAuthenticationToken
		OAuth2ClientAuthenticationToken clientPrincipal = OAuth2AuthenticationProviderUtils
				.getAuthenticatedClientElseThrowInvalidClient(resourceOwnerPasswordAuthenticationToken);
		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

		if (this.logger.isTraceEnabled()) {
			this.logger.trace("Retrieved registered client");
		}

		if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.PASSWORD)) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
		}

		// Default to configured scopes
		Set<String> authorizedScopes = Collections.emptySet();
		if (!CollectionUtils.isEmpty(resourceOwnerPasswordAuthenticationToken.getScopes())) {
			for (String requestedScope : resourceOwnerPasswordAuthenticationToken.getScopes()) {
				// 检查请求的scope是否合法
				if (!registeredClient.getScopes().contains(requestedScope)) {
					throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
				}
			}
			authorizedScopes = new LinkedHashSet<>(resourceOwnerPasswordAuthenticationToken.getScopes());
		}

		// 走UsernamePasswordAuthentication认证模式
		Authentication usernamePasswordAuthentication = getUsernamePasswordAuthentication(
				resourceOwnerPasswordAuthenticationToken);

		if (this.logger.isTraceEnabled()) {
			this.logger.trace("Validated token request parameters");
		}

		// @formatter:off
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(usernamePasswordAuthentication)//会传递到OAuth2TokenCustomizer中通过context.getPrincipal()拿到
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(authorizedScopes)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
				//认证请求Token
                .authorizationGrant(resourceOwnerPasswordAuthenticationToken);
        // @formatter:on

		// @formatter:off
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(usernamePasswordAuthentication.getName())
				//授权成功给予的scope
                .authorizedScopes(authorizedScopes)
				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
				//在这个OAuth2Authorization中存入UsernamePasswordAuthentication
                .attribute(Principal.class.getName(), usernamePasswordAuthentication)
                .attribute(UserDetails.class.getName(), usernamePasswordAuthentication.getPrincipal());
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

		// 认证结束，持久化
		OAuth2Authorization authorization = authorizationBuilder.build();
		this.authorizationService.save(authorization);
		if (logger.isTraceEnabled()) {
			logger.trace("Saved authorization");
		}

		// 原先为经过OAuth2ClientAuthenticationFilter认证后的OAuth2ClientAuthenticationToken
		// 切换当前 Authentication 为 User
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(usernamePasswordAuthentication);
		SecurityContextHolder.setContext(context);

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
		return OAuth2ResourceOwnerPasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

	private Authentication getUsernamePasswordAuthentication(
			OAuth2ResourceOwnerPasswordAuthenticationToken resouceOwnerPasswordAuthentication) {

		Map<String, Object> additionalParameters = resouceOwnerPasswordAuthentication.getAdditionalParameters();

		String username = (String) additionalParameters.get(OAuth2ParameterNames.USERNAME);
		String password = (String) additionalParameters.get(OAuth2ParameterNames.PASSWORD);

		//
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				username, password);
		logger.debug("got usernamePasswordAuthenticationToken=" + usernamePasswordAuthenticationToken);

		Authentication authenticate = null;
		try {
			authenticate = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		}
		catch (AuthenticationException e) {
			OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, e.getMessage(), ERROR_URI);
			throw new OAuth2AuthenticationException(error);
		}
		return authenticate;
	}

}
