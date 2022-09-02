package com.schilings.neiko.common.security.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * <p>
 * 授权类：授权码 刷新令牌 密码 隐式 客户端凭证
 * </p>
 *
 * @author Schilings
 */
@AllArgsConstructor
@Getter
public class SecurityConstants {

	/**
	 * 所有API接口
	 *
	 * @author kong
	 */
	public static final class Api {

		public static String authorize = "/oauth2/authorize";

		public static String token = "/oauth2/token";

		public static String refresh = "/oauth2/refresh";

		public static String revoke = "/oauth2/revoke";

		public static String client_token = "/oauth2/client_token";

		public static String doLogin = "/oauth2/doLogin";

		public static String doConfirm = "/oauth2/doConfirm";

	}

	/**
	 * 所有参数名称
	 *
	 * @author kong
	 */
	public static final class Param {

		public static String response_type = "response_type";

		public static String client_id = "client_id";

		public static String client_secret = "client_secret";

		public static String redirect_uri = "redirect_uri";

		public static String scope = "scope";

		public static String state = "state";

		public static String code = "code";

		public static String token = "token";

		public static String access_token = "access_token";

		public static String refresh_token = "refresh_token";

		public static String grant_type = "grant_type";

		public static String username = "username";

		public static String password = "password";

		public static String name = "name";

		public static String pwd = "pwd";

	}

	/**
	 * 所有返回类型
	 */
	public static final class ResponseType {

		public static String code = "code";

		public static String token = "token";

	}

	/**
	 * 所有授权类型
	 */
	public static final class GrantType {

		public static String authorization_code = "authorization_code";

		public static String refresh_token = "refresh_token";

		public static String password = "password";

		public static String client_credentials = "client_credentials";

		public static String implicit = "implicit";

		public static String mobile = "mobile";

		public static String email = "email";

	}

}
