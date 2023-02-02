package com.schilings.neiko.security.oauth2.authorization.server.component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.Assert;

/**
 * Resolving
 * <a href="https://tools.ietf.org/html/rfc6750#section-1.2" target="_blank">Bearer
 * Token</a>s from the HTTP Cookie.
 *
 * @see HttpServletRequest#getCookies()
 * @see <a href="https://tools.ietf.org/html/rfc6750#section-2" target="_blank">RFC 6750
 * Section 2: Authenticated Requests</a>
 */
public class CookieBearerTokenResolver implements BearerTokenResolver {

	private String cookieName = OAuth2TokenType.ACCESS_TOKEN.getValue();

	public CookieBearerTokenResolver() {
	}

	public CookieBearerTokenResolver(String cookieName) {
		Assert.hasText(cookieName, "cookie name cannot be empty");
		this.cookieName = cookieName;
	}

	@Override
	public String resolve(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}

		for (Cookie cookie : cookies) {
			if (this.cookieName.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}

		return null;
	}

}
