package com.schilings.neiko.extend.sa.token.oauth2.component;

public interface LoginService {

	default Object notLoginView() {
		return null;
	}

	default Object confirmView(String clientId, String scope) {
		return null;
	}

	/**
	 * 授权码（Authorization Code）登录
	 * @param code
	 * @return
	 */
	default Object codeLogin(String code) {
		return null;
	}

	/**
	 * 密码式（Password）登录
	 * @param username 前端请求进来的username
	 * @param password 前端请求进来的password
	 * @return
	 */
	default Object passwordLogin(String username, String password) {
		// do nothing
		return null;
	}

	default Object smsLogin(String phoneNumber, String smsCode) {
		// do nothing
		return null;
	}

	default Object emailLogin(String emailAddrsss, String emailCode) {
		// do nothing
		return null;
	}

	/**
	 * 注销登录
	 * @param loginId
	 */
	default void logout(String loginId) {
		// do nothing
	}

}