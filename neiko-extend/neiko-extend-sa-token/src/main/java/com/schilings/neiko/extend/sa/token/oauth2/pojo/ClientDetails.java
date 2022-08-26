package com.schilings.neiko.extend.sa.token.oauth2.pojo;

import java.io.Serializable;
import java.util.Set;

public interface ClientDetails extends Serializable {

	/**
	 * 客户端ID
	 * @return
	 */
	String getClientId();

	/**
	 * 客户端密码
	 * @return
	 */
	String getClientSecret();

	/**
	 * 应用允许授权的所有URL
	 * @return
	 */
	Set<String> getUrls();

	/**
	 * 客户端作用域
	 * @return
	 */
	Set<String> getScope();

	/**
	 * 此客户端被授权的授权类型。
	 * @return
	 */
	Set<String> getAuthorizedGrantTypes();

	/**
	 * Access-Token 保存的时间(单位秒)
	 * @return
	 */
	Long getAccessTokenTimeout();

	/**
	 * Refresh-Token 保存的时间(单位秒)
	 * @return
	 */
	Long getRefreshTokenTimeout();

}
