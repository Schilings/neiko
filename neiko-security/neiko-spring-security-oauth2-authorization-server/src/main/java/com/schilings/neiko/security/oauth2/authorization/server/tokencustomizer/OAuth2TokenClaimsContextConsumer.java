package com.schilings.neiko.security.oauth2.authorization.server.tokencustomizer;

import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;

import java.util.function.Consumer;

public interface OAuth2TokenClaimsContextConsumer extends Consumer<OAuth2TokenClaimsContext> {

}
