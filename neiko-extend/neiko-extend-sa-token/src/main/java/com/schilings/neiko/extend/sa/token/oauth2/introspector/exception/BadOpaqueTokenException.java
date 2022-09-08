package com.schilings.neiko.extend.sa.token.oauth2.introspector.exception;


public class BadOpaqueTokenException extends OAuth2IntrospectionException {

    public BadOpaqueTokenException(String message) {
        super(message);
    }

    public BadOpaqueTokenException(String message, Throwable cause) {
        super(message, cause);
    }

}
