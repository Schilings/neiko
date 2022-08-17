package com.schilings.neiko.common.core.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>{@code
 *
 * }
 * <p>异常基类</p>
 * </pre>
 *
 * @author Schilings
 */
@Getter
@Setter
public class BaseException extends RuntimeException {

	private String message;

	private Throwable throwable;

	public BaseException(String message) {
		super(message);
		this.message = message;
		throwable = null;
	}

	public BaseException(String message, Throwable throwable) {
		super(message, throwable);
		this.message = message;
	}

}
