package com.schilings.neiko.autoconfigure.shiro.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author <a href="https://github.com/kil1ua">Ken-Chy129</a>
 * @date 2022/8/10 16:35
 */
public class SubjectNotExistException extends AuthenticationException {

	public SubjectNotExistException() {
		super();
	}

	public SubjectNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public SubjectNotExistException(String message) {
		super(message);
	}

	public SubjectNotExistException(Throwable cause) {
		super(cause);
	}

}
