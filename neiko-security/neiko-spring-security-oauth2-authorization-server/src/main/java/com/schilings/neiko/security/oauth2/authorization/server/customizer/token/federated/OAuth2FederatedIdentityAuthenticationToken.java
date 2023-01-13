package com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;

import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OAuth2FederatedIdentityAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

	private final String code;

	private final Set<String> scopes;

	protected OAuth2FederatedIdentityAuthenticationToken(String code, Authentication authentication,
			@Nullable Set<String> scopes, @Nullable Map<String, Object> additionalParameters) {
		super(OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY, authentication, additionalParameters);
		this.code = code;
		this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
	}

	/**
	 * Returns the requested scope(s).
	 * @return the requested scope(s), or an empty {@code Set} if not available
	 */
	public Set<String> getScopes() {
		return this.scopes;
	}

	public String getCode() {
		return code;
	}

}
