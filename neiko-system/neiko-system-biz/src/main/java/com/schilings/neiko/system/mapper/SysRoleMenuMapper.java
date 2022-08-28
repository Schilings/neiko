package com.schilings.neiko.system.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.schilings.neiko.common.model.constants.GlobalConstants;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.system.model.entity.SysMenu;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.entity.SysRoleMenu;

import java.io.Serializable;
import java.util.List;

/**
 *
 * <p>
 * 角色-权限表
 * </p>
 *
 * @author Schilings
 */
public interface SysRoleMenuMapper extends ExtendMapper<SysRoleMenu> {

	/**
	 * 根据菜单ID查询对应的角色
	 * @param MenuId 角色标识
	 * @return List<SysRole>
	 */
	default List<SysRole> listRoleByMenuId(Serializable MenuId) {
		NeikoLambdaQueryWrapper<SysMenu> queryWrapper = WrappersX.<SysMenu>lambdaQueryJoin().selectAll(SysRole.class)
				.leftJoin(SysRole.class, SysRole::getCode, SysRoleMenu::getRoleCode)
				.eq(SysRole::getDeleted, GlobalConstants.NOT_DELETED_FLAG).eq(SysRoleMenu::getMenuId, MenuId);
		return this.selectJoinList(SysRole.class, AUTO_RESULT_MAP, queryWrapper);
	}

	/**
	 * 根据角色标识查询对应的菜单
	 * @param roleCode 角色标识
	 * @return List<SysMenu>
	 */
	default List<SysMenu> listMenuByRoleCode(String roleCode) {
		NeikoLambdaQueryWrapper<SysMenu> queryWrapper = WrappersX.<SysMenu>lambdaQueryJoin().selectAll(SysMenu.class)
				.leftJoin(SysMenu.class, SysMenu::getId, SysRoleMenu::getMenuId)
				.eq(SysMenu::getDeleted, GlobalConstants.NOT_DELETED_FLAG).eq(SysRoleMenu::getRoleCode, roleCode)
				.orderByAsc(SysMenu::getSort);
		return this.selectJoinList(SysMenu.class, AUTO_RESULT_MAP, queryWrapper);
	}

	/**
	 * 根据角色标识删除角色权限关联关系
	 * @param roleCode 角色标识
	 */
	default boolean deleteAllByRoleCode(String roleCode) {
		int delete = this.delete(WrappersX.lambdaQueryX(SysRoleMenu.class).eq(SysRoleMenu::getRoleCode, roleCode));
		return SqlHelper.retBool(delete);
	}

	/**
	 * 根据权限ID删除角色权限关联关系
	 * @param menuId – 权限ID
	 */
	default boolean deleteAllByMenuId(Serializable menuId) {
		int delete = this.delete(WrappersX.lambdaQueryX(SysRoleMenu.class).eq(SysRoleMenu::getMenuId, menuId));
		return SqlHelper.retBool(delete);
	}

	/**
	 * 更新某个菜单的 id
	 * @param originalId 原菜单ID
	 * @param menuId 修改后的菜单Id
	 * @return 被更新的条数
	 */
	default boolean updateMenuId(Long originalId, Long menuId) {
		// @formatter:off
        LambdaUpdateWrapper<SysRoleMenu> wrapper = Wrappers.lambdaUpdate(SysRoleMenu.class)
                .set(SysRoleMenu::getMenuId, menuId)
                .eq(SysRoleMenu::getMenuId, originalId);
        // @formatter:on
		int update = this.update(null, wrapper);
		return SqlHelper.retBool(update);
	}

}
