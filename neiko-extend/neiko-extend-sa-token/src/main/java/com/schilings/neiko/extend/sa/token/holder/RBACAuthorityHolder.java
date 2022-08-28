package com.schilings.neiko.extend.sa.token.holder;

import cn.dev33.satoken.session.SaSessionCustomUtil;
import com.schilings.neiko.extend.sa.token.oauth2.event.authority.PermissionAuthorityChangedEvent;
import com.schilings.neiko.extend.sa.token.oauth2.event.authority.RoleAuthorityChangedEvent;
import com.schilings.neiko.extend.sa.token.core.StpOauth2UserUtil;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.function.Supplier;

public class RBACAuthorityHolder {

	private static final String USERDETAILS = "auth:userdeatils:";

	private static final String ROLES = "auth:roles:";

	private static final String PERMISSIONS = "auth:permissions:";

	@ExceptionHandler
	public void refreshUserRole(RoleAuthorityChangedEvent event) {
		event.getUserId().forEach(RBACAuthorityHolder::deleteRoles);
	}

	@ExceptionHandler
	public void refreshRolePermission(PermissionAuthorityChangedEvent event) {
		event.getRoleCode().forEach(RBACAuthorityHolder::deletePermissions);
	}

	public static void deleteUserDetails(String userId) {
		StpOauth2UserUtil.getSessionByLoginId(userId).delete("USER_DETAILS");

	}

	public static void deleteRoles(String userId) {
		SaSessionCustomUtil.getSessionById(ROLES + userId).delete("ROLES");
	}

	public static void deletePermissions(String roleCode) {
		SaSessionCustomUtil.getSessionById(PERMISSIONS + roleCode).delete("PERMISSIONS");
	}

	public static <T extends UserDetails> void setUserDetails(String userId, T userDetails) {
		StpOauth2UserUtil.getSessionByLoginId(userId).set("USER_DETAILS", userDetails);

	}

	public static void setRoles(String userId, List<String> roles) {
		SaSessionCustomUtil.getSessionById(ROLES + userId).set("ROLES", roles);
	}

	public static void setPermissions(String roleCode, List<String> permissions) {
		SaSessionCustomUtil.getSessionById(PERMISSIONS + roleCode).set("PERMISSIONS", permissions);
	}

	/**
	 * 获取当前登录用户的UserDetails
	 * @param userId
	 * @param <T>
	 * @return
	 */

	public static <T extends UserDetails> T getUserDetails() {
		return (T) StpOauth2UserUtil.getSession().get("USER_DETAILS");
		// return JsonUtils.toObj(value, new TypeReference<T>() {});
	}

	public static <T extends UserDetails> T getUserDetails(String userId) {
		return (T) StpOauth2UserUtil.getSessionByLoginId(userId).get("USER_DETAILS");
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
		return (List<String>) SaSessionCustomUtil.getSessionById(ROLES + userId).get("ROLES");
	}

	public static List<String> getRoles(String userId, Supplier<List<String>> supplier) {
		List<String> value = getRoles(userId);
		if (value == null) {
			List<String> roles = supplier.get();
			setRoles(userId, roles);
			return roles;
		}
		return value;
	}

	/**
	 * 获取Permissions
	 * @param roleCode
	 * @return
	 */
	public static List<String> getPermissions(String roleCode) {
		return (List<String>) SaSessionCustomUtil.getSessionById(PERMISSIONS + roleCode).get("PERMISSIONS");

	}

	public static List<String> getPermissions(String roleCode, Supplier<List<String>> supplier) {
		List<String> value = getPermissions(roleCode);
		if (value == null) {
			List<String> permissions = supplier.get();
			setPermissions(roleCode, permissions);
			return permissions;
		}
		return value;
	}

}
