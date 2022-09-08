package com.schilings.neiko.extend.sa.token.holder;

import cn.dev33.satoken.session.SaSessionCustomUtil;
import com.schilings.neiko.common.security.constant.UserAttributeNameConstants;
import com.schilings.neiko.extend.sa.token.oauth2.event.authority.PermissionAuthorityChangedEvent;
import com.schilings.neiko.extend.sa.token.oauth2.event.authority.RoleAuthorityChangedEvent;
import com.schilings.neiko.extend.sa.token.core.StpOAuth2UserUtil;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.UserDetails;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class RBACAuthorityHolder {

	public static final List<String> NULL_VALUE = Collections.emptyList();

	private static final String USERDETAILS = "auth:userdeatils:";

	private static final String ROLES = "auth:roles:";

	private static final String PERMISSIONS = "auth:permissions:";
	

	public static void deleteUserDetails(String userId) {
		StpOAuth2UserUtil.getSessionByLoginId(userId)
				.delete(UserAttributeNameConstants.USER_DETAILS);
	}

	public static void deleteRoles(String userId) {
		SaSessionCustomUtil.getSessionById(ROLES + userId)
				.delete(UserAttributeNameConstants.ROLE_CODES);
	}

	public static void deletePermissions(String roleCode) {
		SaSessionCustomUtil.getSessionById(PERMISSIONS + roleCode)
				.delete(UserAttributeNameConstants.PERMISSIONS);
	}

	public static <T extends UserDetails> void setUserDetails(String userId, T userDetails) {
		StpOAuth2UserUtil.getSessionByLoginId(userId)
				.set(UserAttributeNameConstants.USER_DETAILS, userDetails);
	}

	public static <T extends UserDetails> void setUserDetails(T userDetails) {
		StpOAuth2UserUtil.getSession()
				.set(UserAttributeNameConstants.USER_DETAILS, userDetails);
	}

	public static void setRoles(String userId, List<String> roles) {
		// 放置于公共缓存
		if (CollectionUtils.isEmpty(roles)) {
			SaSessionCustomUtil.getSessionById(ROLES + userId)
					.set(UserAttributeNameConstants.ROLE_CODES, NULL_VALUE);
		} else {
			SaSessionCustomUtil.getSessionById(ROLES + userId)
					.set(UserAttributeNameConstants.ROLE_CODES, roles);
		} 

	}

	public static void setPermissions(String roleCode, List<String> permissions) {
		// 放置于公共缓存
		if (CollectionUtils.isEmpty(permissions)) {
			SaSessionCustomUtil.getSessionById(PERMISSIONS + roleCode)
					.set(UserAttributeNameConstants.PERMISSIONS, NULL_VALUE);
		} else {
			SaSessionCustomUtil.getSessionById(PERMISSIONS + roleCode)
					.set(UserAttributeNameConstants.PERMISSIONS, permissions);
		} 

	}

	/**
	 * 获取当前登录用户的UserDetails
	 * @param <T>
	 * @return
	 */
	public static <T extends UserDetails> T getUserDetails() {
		if (!StpOAuth2UserUtil.isLogin()) {
			return null;
		}
		return (T) StpOAuth2UserUtil.getSession().get(UserAttributeNameConstants.USER_DETAILS);
		// return JsonUtils.toObj(value, new TypeReference<T>() {});
	}

	public static <T extends UserDetails> T getUserDetails(String userId) {
		if (!StpOAuth2UserUtil.isLogin()) {
			return null;
		}
		return (T) StpOAuth2UserUtil.getSessionByLoginId(userId).get(UserAttributeNameConstants.USER_DETAILS);
		// return JsonUtils.toObj(value, new TypeReference<T>() {});
	}

	public static <T extends UserDetails> T getUserDetails(String userId, Supplier<T> supplier) {
		T value = getUserDetails(userId);
		if (value == null) {
			T userDetails = supplier.get();
			setUserDetails(userId, userDetails);
			return userDetails;
		}
		return value;
	}

	/**
	 * 获取Roles
	 * @param userId
	 * @return
	 */

	public static List<String> getRoles(String userId) {
		return (List<String>) SaSessionCustomUtil.getSessionById(ROLES + userId)
				.get(UserAttributeNameConstants.ROLE_CODES);
	}

	public static List<String> getRoles(String userId, Supplier<List<String>> supplier) {
		List<String> value = getRoles(userId);
		if (value == null) {
			List<String> roles = supplier.get();
			setRoles(userId, roles);
			return roles == null ? NULL_VALUE : roles;
		}
		return value;
	}

	/**
	 * 获取Permissions
	 * @param roleCode
	 * @return
	 */
	public static List<String> getPermissions(String roleCode) {
		return (List<String>) SaSessionCustomUtil.getSessionById(PERMISSIONS + roleCode)
				.get(UserAttributeNameConstants.PERMISSIONS);

	}

	public static List<String> getPermissions(String roleCode, Supplier<List<String>> supplier) {
		List<String> value = getPermissions(roleCode);
		if (value == null) {
			List<String> permissions = supplier.get();
			setPermissions(roleCode, permissions);
			return permissions == null ? NULL_VALUE : permissions;
		}
		return value;
	}

}
