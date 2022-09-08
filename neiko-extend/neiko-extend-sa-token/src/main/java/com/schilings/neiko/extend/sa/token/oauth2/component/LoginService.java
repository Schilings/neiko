package com.schilings.neiko.extend.sa.token.oauth2.component;

public interface LoginService {

	default Object notLoginView() {
		return null;
	}

	default Object confirmView(String clientId, String scope) {
		return null;
	}

	/**
	 * 密码式（Password）登录
	 * @param username 前端请求进来的username
	 * @param password 前端请求进来的password
	 * @return
	 */
	default Object login(String username, String password) {
		// do nothing
		return null;
	}

}
