package com.schilings.neiko.admin.upms.config.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.schilings.neiko.extend.sa.token.holder.RBACAuthorityHolder;
import com.schilings.neiko.system.service.*;
import lombok.RequiredArgsConstructor;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * <p>
 * 自定义权限获取Service
 * </p>
 *
 * @author Schilings
 */
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {
	
	private final SysUserRoleService sysUserRoleService;

	private final SysRoleMenuService sysRoleMenuService;

	@Override
	public List<String> getRoleList(Object userId, String loginType) {
		return RBACAuthorityHolder.getRoles((String) userId, () -> {
			return sysUserRoleService.listRoleCodes(Long.valueOf((String) userId));
		});
	}

	@Override
	public List<String> getPermissionList(Object userId, String loginType) {
		// 1. 声明权限码集合
		List<String> permissionList = new ArrayList<>();
		// 2. 遍历角色列表，查询拥有的权限码
		for (String roleId : getRoleList(userId, loginType)) {
			List<String> permissions = RBACAuthorityHolder.getPermissions(roleId, () -> {
				// 从数据库查询这个角色所拥有的权限列表
				return sysRoleMenuService.listPermissions(roleId);
			});
			permissionList.addAll(new ArrayList<>(permissions));
		}
		return permissionList;
	}

}
