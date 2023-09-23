package com.schilings.neiko.system.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.schilings.neiko.common.util.StringUtils;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.system.mapper.SysRoleMenuMapper;
import com.schilings.neiko.system.model.entity.SysMenu;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.entity.SysRoleMenu;
import com.schilings.neiko.system.service.SysRoleMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleMenuServiceImpl extends ExtendServiceImpl<SysRoleMenuMapper, SysRoleMenu>
		implements SysRoleMenuService {

	private final ApplicationEventPublisher eventPublisher;

	/**
	 * 根据菜单ID查询对应的角色
	 * @param menuId 菜单ID
	 * @return List<SysMenu>
	 */
	public List<SysRole> listRoles(Serializable menuId) {
		return baseMapper.listRoleByMenuId(menuId);
	}

	/**
	 * 根据菜单ID查询对应的角色标识
	 * @param menuId 菜单ID
	 * @return List<String>
	 */
	public List<String> listRoleCodes(Serializable menuId) {
		return baseMapper.listRoleByMenuId(menuId).stream().map(SysRole::getCode).collect(Collectors.toList());
	}

	/**
	 * 根据角色标识查询对应的菜单
	 * @param roleCode 角色标识
	 * @return List<SysMenu>
	 */
	@Override
	public List<SysMenu> listMenus(String roleCode) {
		return baseMapper.listMenuByRoleCode(roleCode);
	}

	/**
	 * 根据角色标识查询对应的授权标识
	 * @param roleCode 角色标识
	 * @return List<String>
	 */
	@Override
	public List<String> listPermissions(String roleCode) {
		return baseMapper.listMenuByRoleCode(roleCode).stream().map(SysMenu::getPermission)
				.filter(StringUtils::isNotBlank).collect(Collectors.toList());
	}

	/**
	 * @param roleCode 角色
	 * @param menuIds 权限ID集合
	 * @return boolean
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveRoleMenus(String roleCode, Long[] menuIds) {
		// 1、先删除旧数据
		baseMapper.deleteAllByRoleCode(roleCode);
		if (menuIds == null || menuIds.length == 0) {
			// 发布角色权限更改事件
			return Boolean.TRUE;
		}

		// 2、再批量插入新数据
		List<SysRoleMenu> list = Arrays.stream(menuIds).map(menuId -> new SysRoleMenu(roleCode, menuId))
				.collect(Collectors.toList());
		int i = baseMapper.insertBatchSomeColumn(list);
		boolean insertSuccess = SqlHelper.retBool(i);
		if (insertSuccess) {
			// 发布角色权限更改事件
			return insertSuccess;
		}
		return insertSuccess;
	}

	/**
	 * 根据权限ID删除角色权限关联数据
	 * @param menuId 权限ID
	 */
	@Override
	public boolean deleteAllByMenuId(Serializable menuId) {
		// 删除前查一下，留着发布事件
		// List<String> roleCodes = this.listRoleCodes(menuId);
		boolean deleteSuccess = baseMapper.deleteAllByMenuId(menuId);
		if (deleteSuccess) {
			// 发布角色权限更改事件
			return deleteSuccess;
		}
		return deleteSuccess;
	}

	/**
	 * 根据角色标识删除角色权限关联关系
	 * @param roleCode 角色标识
	 */
	@Override
	public boolean deleteAllByRoleCode(String roleCode) {
		boolean deleteSuccess = baseMapper.deleteAllByRoleCode(roleCode);
		if (deleteSuccess) {
			// 发布角色权限更改事件
			return deleteSuccess;
		}
		return deleteSuccess;
	}

	/**
	 * 更新某个菜单的 id
	 * @param originalId 原菜单ID
	 * @param menuId 修改后的菜单Id
	 * @return 被更新的菜单数
	 */
	@Override
	public boolean updateMenuId(Long originalId, Long menuId) {
		boolean updateSuccess = baseMapper.updateMenuId(originalId, menuId);
		// List<String> roleCodes = this.listRoleCodes(menuId);
		if (updateSuccess) {
			// 发布角色权限更改事件
			return updateSuccess;
		}
		return updateSuccess;
	}

}
