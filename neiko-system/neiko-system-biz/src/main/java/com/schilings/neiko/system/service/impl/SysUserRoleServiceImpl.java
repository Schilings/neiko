package com.schilings.neiko.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.schilings.neiko.common.core.exception.ServiceException;
import com.schilings.neiko.common.event.publisher.EventBus;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.BaseResultCode;
import com.schilings.neiko.extend.sa.token.oauth2.event.authority.RoleAuthorityChangedEvent;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;

import com.schilings.neiko.system.mapper.SysUserRoleMapper;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.model.entity.SysUserRole;
import com.schilings.neiko.system.model.qo.RoleBindUserQO;
import com.schilings.neiko.system.model.vo.RoleBindUserVO;
import com.schilings.neiko.system.service.SysUserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserRoleServiceImpl extends ExtendServiceImpl<SysUserRoleMapper, SysUserRole>
		implements SysUserRoleService {

	private final EventBus EventBus;

	private final ApplicationEventPublisher eventPublisher;

	/**
	 * 通过角色标识，查询用户列表
	 * @param pageParam 分页参数
	 * @param qo 角色标识
	 * @return PageResult<RoleBindUserVO> 角色授权的用户列表
	 */
	@Override
	public PageResult<RoleBindUserVO> queryUserPage(PageParam pageParam, RoleBindUserQO qo) {
		return baseMapper.queryUserPageByRoleCode(pageParam, qo);
	}

	/**
	 * 通过用户ID，查询角色列表
	 * @param userId 用户ID
	 * @return List<SysRole>
	 */
	@Override
	public List<SysRole> listRoles(Long userId) {
		return baseMapper.listRoleByUserId(userId);
	}

	/**
	 * 获取用户的角色Code集合
	 * @param userId 用户id
	 * @return List<String>
	 */
	public List<String> listRoleCodes(Long userId) {
		return baseMapper.listRoleByUserId(userId).stream().map(SysRole::getCode).collect(Collectors.toList());
	}

	/**
	 * 根据角色查询用户
	 * @param roleCode 角色标识
	 * @return List<SysUser>
	 */
	public List<SysUser> listUsers(String roleCode) {
		return baseMapper.listUserByRoleCodes(Collections.singletonList(roleCode));
	}

	/**
	 * 根据角色查询用户
	 * @param roleCodes 角色标识集合
	 * @return List<SysUser> 用户集合
	 */
	public List<SysUser> listUsers(Collection<String> roleCodes) {
		return baseMapper.listUserByRoleCodes(roleCodes);
	}

	/**
	 * 根据UserId删除该用户角色关联关系
	 * @param userId 用户ID
	 * @return boolean
	 */
	@Override
	public boolean deleteByUserId(Long userId) {
		boolean deleteSuccess = baseMapper.deleteAllByUserId(userId);
		if (deleteSuccess) {
			eventPublisher.publishEvent(new RoleAuthorityChangedEvent(Arrays.asList(userId.toString())));
		}
		return deleteSuccess;
	}

	/**
	 * 解绑角色和用户关系
	 * @param userId 用户ID
	 * @param roleCode 角色标识
	 * @return 解绑成功：true
	 */
	@Override
	public boolean unbindRoleUser(Long userId, String roleCode) {
		// 不存在则不需要进行删除，直接返回true
		if (!baseMapper.existsRoleBind(userId, roleCode)) {
			return true;
		}
		else {
			boolean deleteSuccess = baseMapper.deleteUserRole(userId, roleCode);
			if (deleteSuccess) {
				eventPublisher.publishEvent(new RoleAuthorityChangedEvent(Arrays.asList(userId.toString())));
			}
			return deleteSuccess;
		}
	}

	/**
	 * 更新用户关联关系
	 * @param userId 用户ID
	 * @param roleCodes 角色标识集合
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateUserRoles(Long userId, List<String> roleCodes) {
		// 是否存在用户角色绑定关系，存在则先清空
		boolean existsRoleBind = baseMapper.existsRoleBind(userId, null);
		if (existsRoleBind) {
			boolean deleteSuccess = baseMapper.deleteAllByUserId(userId);
			Assert.isTrue(deleteSuccess, () -> {
				log.error("[updateUserRoles] 删除用户角色关联关系失败，userId：{}，roleCodes：{}", userId, roleCodes);
				return new ServiceException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "删除用户角色关联关系失败");
			});
		}
		// 没有的新授权的角色直接返回
		if (CollectionUtil.isEmpty(roleCodes)) {
			eventPublisher.publishEvent(new RoleAuthorityChangedEvent(Arrays.asList(userId.toString())));
			return true;
		}
		// 保存新的用户角色关联关系
		boolean addSuccess = addUserRoles(userId, roleCodes);
		if (addSuccess) {
			eventPublisher.publishEvent(new RoleAuthorityChangedEvent(Arrays.asList(userId.toString())));
		}
		return addSuccess;
	}

	/**
	 * 插入用户角色关联关系
	 * @param userId 用户ID
	 * @param roleCodes 角色标识集合
	 * @return boolean
	 */
	@Override
	public boolean addUserRoles(Long userId, List<String> roleCodes) {
		List<SysUserRole> list = prodSysUserRoles(userId, roleCodes);
		// 批量插入
		boolean insertSuccess = baseMapper.insertUserRoles(list);
		Assert.isTrue(insertSuccess, () -> {
			log.error("[addUserRoles] 插入用户角色关联关系失败，userId：{}，roleCodes：{}", userId, roleCodes);
			return new ServiceException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "插入用户角色关联关系失败");
		});
		eventPublisher.publishEvent(new RoleAuthorityChangedEvent(Arrays.asList(userId.toString())));
		return insertSuccess;
	}

	/**
	 * 根据用户ID 和 角色Code 生成SysUserRole实体集合
	 * @param userId 用户ID
	 * @param roleCodes 角色标识集合
	 * @return List<SysUserRole>
	 */
	private List<SysUserRole> prodSysUserRoles(Long userId, List<String> roleCodes) {
		// 转换为 SysUserRole 实体集合
		List<SysUserRole> list = new ArrayList<>();
		for (String roleCode : roleCodes) {
			SysUserRole sysUserRole = new SysUserRole();
			sysUserRole.setUserId(userId);
			sysUserRole.setRoleCode(roleCode);
			list.add(sysUserRole);
		}
		return list;
	}

}
