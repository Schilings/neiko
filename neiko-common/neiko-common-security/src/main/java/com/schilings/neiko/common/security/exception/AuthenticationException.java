package com.schilings.neiko.common.security.exception;

/**
 *
 * <p>
 * 登录验证异常
 * </p>
 *
 * @author Schilings
 */
public class AuthenticationException extends SecurityException {

	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

}
