package com.schilings.neiko.system.service;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.model.entity.SysUserRole;
import com.schilings.neiko.system.model.qo.RoleBindUserQO;
import com.schilings.neiko.system.model.vo.RoleBindUserVO;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface SysUserRoleService extends ExtendService<SysUserRole> {

	/**
	 * 通过角色标识，查询用户列表
	 * @param pageParam 分页参数
	 * @param qo 角色标识
	 * @return PageResult<RoleBindUserVO> 角色授权的用户列表
	 */
	PageResult<RoleBindUserVO> queryUserPage(PageParam pageParam, RoleBindUserQO qo);

	/**
	 * 通过用户ID，查询角色列表
	 * @param userId 用户ID
	 * @return List<SysRole>
	 */
	List<SysRole> listRoles(Long userId);

	/**
	 * 获取用户的角色Code集合
	 * @param userId 用户id
	 * @return List<String>
	 */
	List<String> listRoleCodes(Long userId);

	/**
	 * 根据角色查询用户
	 * @param roleCode 角色标识
	 * @return List<SysUser>
	 */
	List<SysUser> listUsers(String roleCode);

	/**
	 * 根据角色查询用户
	 * @param roleCodes 角色标识集合
	 * @return List<SysUser> 用户集合
	 */
	List<SysUser> listUsers(Collection<String> roleCodes);

	/**
	 * 删除用户的角色
	 * @param userId 用户ID
	 * @return 删除是否成功
	 */
	boolean deleteByUserId(Long userId);

	/**
	 * 解绑角色和用户关系
	 * @param userId 用户ID
	 * @param roleCode 角色标识
	 * @return 解绑成功：true
	 */
	boolean unbindRoleUser(Long userId, String roleCode);

	/**
	 * 更新用户关联关系
	 * @param userId 用户ID
	 * @param roleCodes 角色标识集合
	 * @return boolean
	 */
	boolean updateUserRoles(Long userId, List<String> roleCodes);

	/**
	 * 添加用户角色关联关系
	 * @param userId 用户ID
	 * @param roleCodes 角色标识集合
	 * @return 插入是否成功
	 */
	boolean addUserRoles(Long userId, List<String> roleCodes);

}
