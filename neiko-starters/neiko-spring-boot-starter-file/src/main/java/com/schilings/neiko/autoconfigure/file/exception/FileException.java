package com.schilings.neiko.autoconfigure.file.exception;

import java.io.IOException;

/**
 * 文件系统异常
 *
 */
public class FileException extends IOException {

	public FileException() {
	}

	public FileException(String message) {
		super(message);
	}

	public FileException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileException(Throwable cause) {
		super(cause);
	}

}
