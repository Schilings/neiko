package com.schilings.neiko.security.oauth2.authorization.server.customizer.token;

import org.springframework.security.web.authentication.AuthenticationConverter;

@FunctionalInterface
public interface OAuth2ExtensionGrantTypeAuthenticationConverter extends AuthenticationConverter {

}
