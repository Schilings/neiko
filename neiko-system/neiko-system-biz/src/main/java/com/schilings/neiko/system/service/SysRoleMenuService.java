package com.schilings.neiko.system.service;

import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import com.schilings.neiko.system.model.entity.SysMenu;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.entity.SysRoleMenu;

import java.util.List;

public interface SysRoleMenuService extends ExtendService<SysRoleMenu> {

	/**
	 * 根据角色标识查询对应的菜单
	 * @param roleCode 角色标识
	 * @return List<SysMenu>
	 */
	List<SysMenu> listByRoleCode(String roleCode);

	/**
	 * 根据角色标识查询对应的授权标识
	 * @param roleCode 角色标识
	 * @return List<String>
	 */
	List<String> listPermissionByRoleCode(String roleCode);

}
