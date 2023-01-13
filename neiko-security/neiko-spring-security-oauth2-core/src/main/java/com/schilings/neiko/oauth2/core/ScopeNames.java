package com.schilings.neiko.oauth2.core;

public final class ScopeNames {

	private ScopeNames() {
	}

	/**
	 * 跳过验证码
	 */
	public static final String SKIP_CAPTCHA = "skip_captcha";

	/**
	 * 跳过密码解密 （使用明文传输）
	 */
	public static final String SKIP_PASSWORD_DECODE = "skip_password_decode";

}
