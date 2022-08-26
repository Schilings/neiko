package com.schilings.neiko.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.schilings.neiko.common.event.publisher.EventBus;
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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleMenuServiceImpl extends ExtendServiceImpl<SysRoleMenuMapper, SysRoleMenu>
		implements SysRoleMenuService {

	private final EventBus eventBus;

	private final ApplicationEventPublisher eventPublisher;

	/**
	 * 根据角色标识查询对应的菜单
	 * @param roleCode 角色标识
	 * @return List<SysMenu>
	 */
	@Override
	public List<SysMenu> listByRoleCode(String roleCode) {
		return baseMapper.listByRoleCode(roleCode);
	}

	@Override
	public List<String> listPermissionByRoleCode(String roleCode) {
		return baseMapper.listByRoleCode(roleCode).stream().map(SysMenu::getPermission).filter(StrUtil::isNotEmpty)
				.collect(Collectors.toList());
	}

}
