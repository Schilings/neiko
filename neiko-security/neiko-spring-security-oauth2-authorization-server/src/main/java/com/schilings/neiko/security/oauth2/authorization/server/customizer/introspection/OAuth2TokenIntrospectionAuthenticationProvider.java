package com.schilings.neiko.security.oauth2.authorization.server.customizer.introspection;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 *
 * <p>
 * Copy From SAS
 * </p>
 *
 * @author Schilings
 */
public class OAuth2TokenIntrospectionAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return false;
	}

}
