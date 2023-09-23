package com.schilings.neiko.security.oauth2.core;

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

	/**
	 * TokenClaim附带权限信息
	 */
	public static final String AUTHORITY_INFO_CLAIM = "authority_info_claim";

	/**
	 * TokenClaim附带用户信息
	 */
	public static final String USER_INFO_CLAIM = "user_info_claim";

}
