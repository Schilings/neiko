package com.schilings.neiko.system.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.schilings.neiko.common.model.constants.GlobalConstants;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.query.LambdaQueryWrapperX;
import com.schilings.neiko.system.model.entity.SysMenu;
import com.schilings.neiko.system.model.qo.SysMenuQO;

import java.io.Serializable;
import java.util.List;

/**
 *
 * <p>
 * 系统权限表
 * </p>
 *
 * @author Schilings
 */
public interface SysMenuMapper extends ExtendMapper<SysMenu> {

	/**
	 * 查询权限集合，并按sort排序（升序）
	 * @param sysMenuQO 查询条件
	 * @return List<SysMenu>
	 */
	default List<SysMenu> listOrderBySort(SysMenuQO sysMenuQO) {
		LambdaQueryWrapperX<SysMenu> queryWrapper = WrappersX.lambdaQueryX(SysMenu.class)
				.eq(SysMenu::getDeleted, GlobalConstants.NOT_DELETED_FLAG)
				.likeIfPresent(SysMenu::getId, sysMenuQO.getId()).likeIfPresent(SysMenu::getTitle, sysMenuQO.getTitle())
				.likeIfPresent(SysMenu::getPermission, sysMenuQO.getPermission())
				.likeIfPresent(SysMenu::getPath, sysMenuQO.getPath());
		queryWrapper.orderByAsc(SysMenu::getSort);
		return this.selectList(queryWrapper);
	}

	/**
	 * 查询指定权限的下级权限总数
	 * @param id 权限ID
	 * @return 下级权限总数
	 */
	default Long countSubMenu(Serializable id) {
		return this.selectCount(Wrappers.<SysMenu>query().lambda()
				.eq(SysMenu::getDeleted, GlobalConstants.NOT_DELETED_FLAG).eq(SysMenu::getParentId, id));
	}

	/**
	 * 根据指定的 id 更新 菜单权限（便于修改其 id）
	 * @param sysMenu 系统菜单
	 * @param originalId 原菜单ID
	 * @return 更新成功返回 true
	 */
	default boolean updateMenuAndId(Long originalId, SysMenu sysMenu) {
		// @formatter:off
        LambdaUpdateWrapper<SysMenu> wrapper = Wrappers.lambdaUpdate(SysMenu.class)
                .set(SysMenu::getId, sysMenu.getId())
                .eq(SysMenu::getId, originalId);
        // @formatter:on
		int flag = this.update(sysMenu, wrapper);
		return SqlHelper.retBool(flag);
	}

	/**
	 * (更改所有子菜单的父菜单) 根据指定的 parentId 找到对应的菜单，更新其 parentId
	 * @param originalParentId 原 parentId
	 * @param parentId 现 parentId
	 * @return 更新条数不为 0 时，返回 true
	 */
	default boolean updateParentId(Long originalParentId, Long parentId) {
		// @formatter:off
        LambdaUpdateWrapper<SysMenu> wrapper = Wrappers.lambdaUpdate(SysMenu.class)
                .set(SysMenu::getParentId, parentId)
                .eq(SysMenu::getParentId, originalParentId);
        // @formatter:on
		int flag = this.update(null, wrapper);
		return SqlHelper.retBool(flag);
	}

}
