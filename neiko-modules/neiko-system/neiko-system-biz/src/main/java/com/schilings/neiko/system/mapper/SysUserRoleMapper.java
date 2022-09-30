package com.schilings.neiko.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.schilings.neiko.common.model.constants.GlobalConstants;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;

import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.system.model.entity.SysOrganization;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.model.entity.SysUserRole;
import com.schilings.neiko.system.model.qo.RoleBindUserQO;
import com.schilings.neiko.system.model.vo.RoleBindUserVO;

import java.util.Collection;
import java.util.List;

import static com.schilings.neiko.common.model.constants.GlobalConstants.NOT_DELETED_FLAG;

/**
 *
 * <p>
 * 用户-角色表
 * </p>
 *
 * @author Schilings
 */
public interface SysUserRoleMapper extends ExtendMapper<SysUserRole> {

	/**
	 * 删除用户的所有角色关联关系
	 * @param userId 用户ID
	 * @return boolean 删除是否成功
	 */
	default boolean deleteAllByUserId(Long userId) {
		int delete = this.delete(WrappersX.lambdaQueryX(SysUserRole.class).eq(SysUserRole::getUserId, userId));
		return SqlHelper.retBool(delete);
	}

	/**
	 * 批量插入用户角色关联关系
	 * @param list 用户角色关联集合
	 * @return boolean 插入是否成功
	 */
	default boolean insertUserRoles(List<SysUserRole> list) {
		int i = this.insertBatchSomeColumn(list);
		return SqlHelper.retBool(i);
	}

	/**
	 * 用户是否存在角色绑定关系
	 * @param userId 用户ID
	 * @param roleCode 角色标识，可为空
	 * @return 存在：true
	 */
	default boolean existsRoleBind(Long userId, String roleCode) {
		Long num = this.selectCount(WrappersX.lambdaQueryX(SysUserRole.class).eq(SysUserRole::getUserId, userId)
				.eqIfPresent(SysUserRole::getRoleCode, roleCode));
		return SqlHelper.retBool(num);
	}

	/**
	 * 删除角色和用户关系
	 * @param userId 用户ID
	 * @param roleCode 角色标识
	 * @return 删除成功：true
	 */
	default boolean deleteUserRole(Long userId, String roleCode) {
		int i = this.delete(Wrappers.lambdaQuery(SysUserRole.class).eq(SysUserRole::getUserId, userId)
				.eq(SysUserRole::getRoleCode, roleCode));
		return SqlHelper.retBool(i);
	}

	/**
	 * 通过角色标识，查询用户列表
	 * @param qo 角色标识
	 * @return PageResult<RoleBindUserVO> 角色授权的用户列表
	 */
	default PageResult<RoleBindUserVO> queryUserPageByRoleCode(PageParam pageParam, RoleBindUserQO qo) {
		IPage<RoleBindUserVO> page = this.prodPage(pageParam);
		NeikoLambdaQueryWrapper<Object> queryWrapper = WrappersX.lambdaQueryJoin().select(SysUser::getUserId)
				.select(SysUser::getUsername).select(SysUser::getNickname).select(SysUser::getOrganizationId)
				.select(SysUser::getType).selectAs(SysOrganization::getName, RoleBindUserVO::getOrganizationName)
				.leftJoin(SysUser.class, SysUser::getUserId, SysUserRole::getUserId)
				.leftJoin(SysOrganization.class, SysOrganization::getId, SysUser::getOrganizationId)
				.eq(SysUser::getDeleted, NOT_DELETED_FLAG).eq(SysUserRole::getRoleCode, qo.getRoleCode())
				.eqIfPresent(SysUser::getUserId, qo.getUserId()).eqIfPresent(SysUser::getUsername, qo.getUsername())
				.eqIfPresent(SysOrganization::getId, qo.getOrganizationId());
		IPage<RoleBindUserVO> iPage = this.selectJoinPage(page, RoleBindUserVO.class, AUTO_RESULT_MAP, queryWrapper);
		return this.prodPage(iPage);

	}

	/**
	 * 根据RoleCode 查询对应用户
	 * @param roleCodes 角色标识
	 * @return List<SysUser> 该角色标识对应的用户列表
	 */
	default List<SysUser> listUserByRoleCodes(Collection<String> roleCodes) {
		/*
		 * SELECT <include refid="Base_Alias_Column_List"/> FROM sys_user su WHERE EXISTS
		 * ( SELECT 1 from nk_sys_user_role ur where ur.`role_code` in <foreach
		 * collection="roleCodes" item="roleCode" separator="," open="(" close=")">
		 * #{roleCode} </foreach> and ur.user_id = su.user_id ) AND su.deleted = 0
		 */
		return this.selectJoinList(SysUser.class, AUTO_RESULT_MAP, WrappersX.lambdaQueryJoin().selectAll(SysUser.class)
				.distinct().rightJoin(SysUser.class, SysUser::getUserId, SysUserRole::getUserId)
				.eq(SysUser::getDeleted, GlobalConstants.NOT_DELETED_FLAG).in(SysUserRole::getRoleCode, roleCodes));
	}

	/**
	 * 通过用户ID，查询角色列表
	 * @param userId 用户ID
	 * @return List<SysRole>
	 */
	default List<SysRole> listRoleByUserId(Long userId) {
		return this.selectJoinList(SysRole.class, AUTO_RESULT_MAP,
				WrappersX.lambdaQueryJoin().selectAll(SysRole.class)
						.rightJoin(SysRole.class, SysRole::getCode, SysUserRole::getRoleCode)
						.eq(SysRole::getDeleted, NOT_DELETED_FLAG).eq(SysUserRole::getUserId, userId));
	}

}
