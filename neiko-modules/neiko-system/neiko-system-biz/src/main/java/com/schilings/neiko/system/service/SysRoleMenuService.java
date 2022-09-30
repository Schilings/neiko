package com.schilings.neiko.system.service;

import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import com.schilings.neiko.system.model.entity.SysMenu;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.entity.SysRoleMenu;

import java.io.Serializable;
import java.util.List;

public interface SysRoleMenuService extends ExtendService<SysRoleMenu> {

	/**
	 * 根据菜单ID查询对应的角色
	 * @param menuId 菜单ID
	 * @return List<SysMenu>
	 */
	List<SysRole> listRoles(Serializable menuId);

	/**
	 * 根据菜单ID查询对应的角色标识
	 * @param menuId 菜单ID
	 * @return List<String>
	 */
	List<String> listRoleCodes(Serializable menuId);

	/**
	 * 根据角色标识查询对应的菜单
	 * @param roleCode 角色标识
	 * @return List<SysMenu>
	 */
	List<SysMenu> listMenus(String roleCode);

	/**
	 * 根据角色标识查询对应的授权标识
	 * @param roleCode 角色标识
	 * @return List<String>
	 */
	List<String> listPermissions(String roleCode);

	/**
	 * 更新角色菜单
	 * @param roleCode 角色
	 * @param menuIds 权限ID数组
	 * @return 更新角色权限关联关系是否成功
	 */
	Boolean saveRoleMenus(String roleCode, Long[] menuIds);

	/**
	 * 根据权限ID删除角色权限关联数据
	 * @param menuId 权限ID
	 */
	boolean deleteAllByMenuId(Serializable menuId);

	/**
	 * 根据角色标识删除角色权限关联关系
	 * @param roleCode 角色标识
	 */
	boolean deleteAllByRoleCode(String roleCode);

	/**
	 * 更新某个菜单的 id
	 * @param originalId 原菜单ID
	 * @param menuId 修改后的菜单Id
	 * @return 被更新的菜单数
	 */
	boolean updateMenuId(Long originalId, Long menuId);

}
