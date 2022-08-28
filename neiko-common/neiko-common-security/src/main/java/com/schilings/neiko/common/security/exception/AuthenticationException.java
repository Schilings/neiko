package com.schilings.neiko.common.security.exception;

/**
 *
 *
 * @author Schilings
 */
public abstract class AuthenticationException extends SecurityException {

	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

}
