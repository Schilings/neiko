package com.schilings.neiko.extend.sa.token.oauth2.pojo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 *
 * <p>
 * 提供核心用户信息。
 * </p>
 *
 * @author Schilings
 */
public interface UserDetails extends Serializable {

	/**
	 * 开放ID
	 * @return
	 */
	String getOpenId();

	/**
	 * 用户ID
	 * @return
	 */
	String getUserId();

	/**
	 * 用户名
	 * @return
	 */
	String getUsername();

	/**
	 * 用户密码
	 * @return
	 */
	String getPassword();

	/**
	 * 盐
	 * @return
	 */
	String getSalt();

	/**
	 * 返回授予用户的权限
	 * @return
	 */
	Collection<? extends GrantedAuthority> getAuthorities();

	/**
	 * 返回用户的权限标识符
	 * @return
	 */
	Collection<PermissionAuthority> getPermissions();

	/**
	 * 返回用户的角色标识符
	 * @return
	 */
	Collection<RoleAuthority> getRoles();


	Map<String, Object> getAttributes();

	/**
	 * 是否可用
	 * @return
	 */
	boolean isEnabled();

}
