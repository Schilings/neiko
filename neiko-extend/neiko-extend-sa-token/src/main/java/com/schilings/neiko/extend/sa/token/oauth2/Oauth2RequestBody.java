package com.schilings.neiko.extend.sa.token.oauth2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * <p>
 * Oauth2请求体，为兼容Post
 * </p>
 *
 * @author Schilings
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Oauth2RequestBody {
	

	/**
	 * 返回类型
	 */
	private String response_type;

	/**
	 * 客户端ID
	 */
	private String client_id;

	/**
	 * 客户端密码
	 */
	private String client_secret;

	private String redirect_uri;

	private String scope;

	private String token;

	private String access_token;

	private String refresh_token;

	/**
	 * 授权方式
	 */
	private String grant_type;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 用户密码
	 */
	private String password;

	/**
	 * 授权码
	 */
	private String code;

	private String name;

	private String pwd;

}
