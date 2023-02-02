package com.schilings.neiko.sample.resource.server.http.token;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

@Component
public class TokenHolder {

    public String getTokenWithPrefix() {
        return BEARER.getValue() + " " + getToken();
    }
    public String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication instanceof BearerTokenAuthentication) {
                BearerTokenAuthentication bearerTokenAuthentication = (BearerTokenAuthentication) authentication;
                return bearerTokenAuthentication.getToken().getTokenValue();
            }
            if (authentication instanceof JwtAuthenticationToken) {
                JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
                return jwtAuthenticationToken.getToken().getTokenValue();
            }
            if (authentication instanceof AbstractOAuth2TokenAuthenticationToken<?>) {
                AbstractOAuth2TokenAuthenticationToken<?> oAuth2TokenAuthenticationToken = (AbstractOAuth2TokenAuthenticationToken<?>) authentication;
                return oAuth2TokenAuthenticationToken.getToken().getTokenValue();
            }
        }
        return null;
    }

}
