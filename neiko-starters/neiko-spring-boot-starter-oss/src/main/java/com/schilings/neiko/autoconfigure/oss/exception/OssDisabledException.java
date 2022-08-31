package com.schilings.neiko.autoconfigure.oss.exception;


public class OssDisabledException extends RuntimeException {

	public OssDisabledException() {
		super("oss 未启用!");
	}

}
