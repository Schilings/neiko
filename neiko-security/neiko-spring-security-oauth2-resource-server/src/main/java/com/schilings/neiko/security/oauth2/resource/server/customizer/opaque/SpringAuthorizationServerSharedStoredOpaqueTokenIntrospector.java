package com.schilings.neiko.security.oauth2.resource.server.customizer.opaque;

import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.security.Principal;
import java.util.*;

@RequiredArgsConstructor
public class SpringAuthorizationServerSharedStoredOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

	private static final String AUTHORITY_SCOPE_PREFIX = "SCOPE_";

	private final OAuth2AuthorizationService authorizationService;

	private final RegisteredClientRepository registeredClientRepository;

	@Override
	public OAuth2AuthenticatedPrincipal introspect(String accessTokenValue) {
		OAuth2Authorization oAuth2Authorization = authorizationService.findByToken(accessTokenValue,
				OAuth2TokenType.ACCESS_TOKEN);
		if (oAuth2Authorization == null) {
			throw new BadOpaqueTokenException("Invalid access token: " + accessTokenValue);
		}

		HashMap<String, Object> claims = new HashMap<>();
		Set<GrantedAuthority> authorities = new HashSet<>();

		OAuth2Authorization.Token<OAuth2AccessToken> token = oAuth2Authorization.getToken(OAuth2AccessToken.class);
		if (token == null) {
			return null;
		}
		claims.putAll(token.getClaims());
		claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.SCOPE, (k, v) -> {
			if (v instanceof String) {
				Collection<String> scopes = Arrays.asList(((String) v).split(" "));
				for (String scope : scopes) {
					authorities.add(new SimpleGrantedAuthority(AUTHORITY_SCOPE_PREFIX + scope));
				}
				return scopes;
			}
			else if (v instanceof Collection) {
				for (String scope : (Collection<String>) v) {
					authorities.add(new SimpleGrantedAuthority(AUTHORITY_SCOPE_PREFIX + scope));
				}
			}
			return v;
		});
		oAuth2Authorization.getAuthorizedScopes().forEach(scope -> {
			authorities.add(new SimpleGrantedAuthority(AUTHORITY_SCOPE_PREFIX + scope));
		});

		AuthorizationGrantType authorizationGrantType = oAuth2Authorization.getAuthorizationGrantType();
		OAuth2IntrospectionAuthenticatedPrincipal introspectionAuthenticatedPrincipal = null;
		if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(authorizationGrantType)) {
			introspectionAuthenticatedPrincipal = getClientPrincipal(oAuth2Authorization, claims, authorities);
		}
		if (AuthorizationGrantType.PASSWORD.equals(authorizationGrantType)) {
			introspectionAuthenticatedPrincipal = getUserDetailAuthenticatedPrincipal(oAuth2Authorization, claims,
					authorities);
		}
		if (OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY.equals(authorizationGrantType)) {
			introspectionAuthenticatedPrincipal = getOAuth2UserAuthenticatedPrincipal(oAuth2Authorization, claims,
					authorities);
		}

		return (introspectionAuthenticatedPrincipal != null) ? introspectionAuthenticatedPrincipal
				: new OAuth2IntrospectionAuthenticatedPrincipal(oAuth2Authorization.getPrincipalName(), claims, authorities);
	}

	private OAuth2IntrospectionAuthenticatedPrincipal getOAuth2UserAuthenticatedPrincipal(
			OAuth2Authorization authorization, Map<String, Object> claims, Collection<GrantedAuthority> authorities) {
		// neiko federated_identity模式传递
		AbstractAuthenticationToken authenticationToken = (AbstractAuthenticationToken) authorization.getAttributes()
				.get(Principal.class.getName());
		if (authenticationToken != null
				&& OAuth2AuthenticationToken.class.isAssignableFrom(authenticationToken.getClass())) {
			Object principal = authenticationToken.getPrincipal();
			if (principal instanceof OAuth2User) {
				OAuth2User oAuth2User = (OAuth2User) principal;
				//OAuth2IntrospectionAuthenticatedPrincipal是BearerTokenAuthentication的Principal
				//BearerTokenAuthentication会把OAuth2IntrospectionAuthenticatedPrincipal的attributes同步给自己
				claims.putAll(authorization.getAttributes());
				authorities.addAll(oAuth2User.getAuthorities());
				return new OAuth2IntrospectionAuthenticatedPrincipal(authorization.getPrincipalName(), claims,
						authorities);
			}
		}
		return null;
	}

	private OAuth2IntrospectionAuthenticatedPrincipal getUserDetailAuthenticatedPrincipal(
			OAuth2Authorization authorization, Map<String, Object> claims, Collection<GrantedAuthority> authorities) {
		// neiko password模式传递
		AbstractAuthenticationToken authenticationToken = (AbstractAuthenticationToken) authorization.getAttributes()
				.get(Principal.class.getName());
		if (authenticationToken != null
				&& UsernamePasswordAuthenticationToken.class.isAssignableFrom(authenticationToken.getClass())) {
			Object principal = authenticationToken.getPrincipal();
			if (principal instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) principal;
				claims.putAll(authorization.getAttributes());
				authorities.addAll(userDetails.getAuthorities());
				return new OAuth2IntrospectionAuthenticatedPrincipal(authorization.getPrincipalName(), claims,
						authorities);
			}
		}
		return null;
	}

	private OAuth2IntrospectionAuthenticatedPrincipal getClientPrincipal(OAuth2Authorization authorization,
			Map<String, Object> claims, Collection<GrantedAuthority> authorities) {
		return new OAuth2IntrospectionAuthenticatedPrincipal(authorization.getPrincipalName(), claims, authorities);
	}

}
