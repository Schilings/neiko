package com.schilings.neiko.common.websocket.exception;

/**
 * 错误的 json 消息
 */
public class ErrorJsonMessageException extends RuntimeException {

	public ErrorJsonMessageException(String message) {
		super(message);
	}

}
