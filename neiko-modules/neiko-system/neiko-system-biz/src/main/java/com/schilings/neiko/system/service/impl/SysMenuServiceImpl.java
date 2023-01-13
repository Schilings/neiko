package com.schilings.neiko.system.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.schilings.neiko.common.core.exception.ServiceException;

import com.schilings.neiko.common.model.result.BaseResultCode;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.system.converter.SysMenuConverter;
import com.schilings.neiko.system.mapper.SysMenuMapper;
import com.schilings.neiko.system.model.dto.SysMenuCreateDTO;
import com.schilings.neiko.system.model.dto.SysMenuUpdateDTO;
import com.schilings.neiko.system.model.entity.SysMenu;
import com.schilings.neiko.system.model.qo.SysMenuQO;
import com.schilings.neiko.system.service.SysMenuService;
import com.schilings.neiko.system.service.SysRoleMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ExtendServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

	private final SysRoleMenuService sysRoleMenuService;

	private final ApplicationEventPublisher eventPublisher;

	/**
	 * 查询权限集合，并按sort排序（升序）
	 * @param sysMenuQO 查询条件
	 * @return List<SysMenu>
	 */
	@Override
	public List<SysMenu> listOrderBySort(SysMenuQO sysMenuQO) {
		return baseMapper.listOrderBySort(sysMenuQO);
	}

	/**
	 * 插入一条记录（选择字段，策略插入）
	 * @param sysMenu 实体对象
	 */
	@Override
	public boolean save(SysMenu sysMenu) {
		Long menuId = sysMenu.getId();
		SysMenu existingMenu = baseMapper.selectById(menuId);
		if (existingMenu != null) {
			String errorMessage = String.format("ID [%s] 已被菜单 [%s] 使用，请更换其他菜单ID", menuId, existingMenu.getTitle());
			throw new ServiceException(BaseResultCode.LOGIC_CHECK_ERROR.getCode(), errorMessage);
		}
		return SqlHelper.retBool(baseMapper.insert(sysMenu));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		// 查询当前权限是否有子权限
		Long subMenu = baseMapper.countSubMenu(id);
		if (subMenu != null && subMenu > 0) {
			throw new ServiceException(BaseResultCode.LOGIC_CHECK_ERROR.getCode(), "菜单含有下级不能删除");
		}
		// 删除角色权限关联数据
		sysRoleMenuService.deleteAllByMenuId(id);
		// 删除当前菜单及其子菜单
		return SqlHelper.retBool(baseMapper.deleteById(id));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysMenuUpdateDTO sysMenuUpdateDTO) {
		// 原来的菜单 Id
		Long originalId = sysMenuUpdateDTO.getOriginalId();
		SysMenu sysMenu = SysMenuConverter.INSTANCE.updateDtoToPo(sysMenuUpdateDTO);

		// 更新菜单信息
		boolean updateSuccess = baseMapper.updateMenuAndId(originalId, sysMenu);
		Assert.isTrue(updateSuccess, () -> {
			log.error("[update] 更新菜单权限时，sql 执行异常，originalId：{}，sysMenu：{}", originalId, sysMenu);
			return new ServiceException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "更新菜单权限时，sql 执行异常");
		});

		// 如果未修改过 菜单id 直接返回
		Long menuId = sysMenuUpdateDTO.getId();
		if (originalId.equals(menuId)) {
			return;
		}

		// 修改过菜单id，则需要对应修改角色菜单的关联表信息，这里不需要 check，因为可能没有授权过该菜单，所以返回值为 0
		sysRoleMenuService.updateMenuId(originalId, menuId);
		// 更新子菜单的 parentId
		baseMapper.updateParentId(originalId, menuId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean create(SysMenuCreateDTO sysMenuCreateDTO) {

		SysMenu sysMenu = SysMenuConverter.INSTANCE.createDtoToPo(sysMenuCreateDTO);
		Long menuId = sysMenu.getId();
		SysMenu existingMenu = baseMapper.selectById(menuId);
		if (existingMenu != null) {
			String errorMessage = String.format("ID [%s] 已被菜单 [%s] 使用，请更换其他菜单ID", menuId, existingMenu.getTitle());
			throw new ServiceException(BaseResultCode.LOGIC_CHECK_ERROR.getCode(), errorMessage);
		}

		boolean saveSuccess = SqlHelper.retBool(baseMapper.insert(sysMenu));
		Assert.isTrue(saveSuccess, () -> {
			log.error("[create] 创建菜单失败，sysMenuCreateDTO: {}", sysMenuCreateDTO);
			return new ServiceException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "创建菜单失败");
		});

		return saveSuccess;
	}

}
