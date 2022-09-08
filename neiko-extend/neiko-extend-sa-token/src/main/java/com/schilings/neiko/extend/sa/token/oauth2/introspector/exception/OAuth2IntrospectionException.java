package com.schilings.neiko.extend.sa.token.oauth2.introspector.exception;

import com.schilings.neiko.common.security.exception.SecurityException;

public class OAuth2IntrospectionException extends SecurityException {

	public OAuth2IntrospectionException(String message) {
		super(message);
	}

	public OAuth2IntrospectionException(String message, Throwable cause) {
		super(message, cause);
	}

}
