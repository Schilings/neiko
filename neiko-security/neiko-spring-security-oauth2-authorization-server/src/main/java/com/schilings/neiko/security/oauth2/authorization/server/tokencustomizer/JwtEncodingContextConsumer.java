package com.schilings.neiko.security.oauth2.authorization.server.tokencustomizer;

import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;

import java.util.function.Consumer;

public interface JwtEncodingContextConsumer extends Consumer<JwtEncodingContext> {

}
