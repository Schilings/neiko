package com.schilings.neiko.security.oauth2.resource.server.customizer.opaque;

import com.schilings.neiko.security.oauth2.core.ScopeNames;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.*;

public class DefaultOpaqueTokenAuthenticationConverter implements OpaqueTokenAuthenticationConverter {

	private final Log logger = LogFactory.getLog(getClass());

	@Override
	public Authentication convert(String introspectedToken, OAuth2AuthenticatedPrincipal authenticatedPrincipal) {
		Instant iat = authenticatedPrincipal.getAttribute(OAuth2TokenIntrospectionClaimNames.IAT);
		Instant exp = authenticatedPrincipal.getAttribute(OAuth2TokenIntrospectionClaimNames.EXP);
		OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, introspectedToken,
				iat, exp);

		// 增加对ScopeNames.AUTHORITY_INFO的处理
		Set<GrantedAuthority> authorities = new HashSet<>(authenticatedPrincipal.getAuthorities());
		for (String authority : getAuthorityInfo(authenticatedPrincipal)) {
			authorities.add(new SimpleGrantedAuthority(authority));
		}
		return new BearerTokenAuthentication(authenticatedPrincipal, accessToken, authorities);
	}

	private Collection<String> getAuthorityInfo(OAuth2AuthenticatedPrincipal authenticatedPrincipal) {
		if (this.logger.isTraceEnabled()) {
			this.logger.trace(LogMessage.format("Looking for %s in attribute %s", ScopeNames.AUTHORITY_INFO,
					ScopeNames.AUTHORITY_INFO));
		}
		Object authorities = authenticatedPrincipal.getAttribute(ScopeNames.AUTHORITY_INFO);
		if (authorities instanceof String) {
			if (StringUtils.hasText((String) authorities)) {
				return Arrays.asList(((String) authorities).split(" "));
			}
			return Collections.emptyList();
		}
		if (authorities instanceof Collection) {
			return castAuthoritiesToCollection(authorities);
		}
		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	private Collection<String> castAuthoritiesToCollection(Object authorities) {
		return (Collection<String>) authorities;
	}

}
