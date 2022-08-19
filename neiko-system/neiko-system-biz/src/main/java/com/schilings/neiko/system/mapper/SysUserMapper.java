package com.schilings.neiko.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.schilings.neiko.common.model.constants.GlobalConstants;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.system.model.entity.SysOrganization;
import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.model.entity.SysUserRole;
import com.schilings.neiko.system.model.qo.SysUserQO;
import com.schilings.neiko.system.model.vo.SysUserPageVO;


import java.util.Collection;
import java.util.List;

/**
 * 
 * <p>系统用户表</p>
 * 
 * @author Schilings
*/
public interface SysUserMapper extends ExtendMapper<SysUser> {

    /**
     * 分页查询
     * @param pageParam 分页参数
     * @param qo 查询对象
     * @return PageResult<SysUserVO>
     */
     default PageResult<SysUserPageVO> queryPage(PageParam pageParam, SysUserQO qo){
         IPage<SysUserPageVO> page = this.prodPage(pageParam);
         NeikoLambdaQueryWrapper<Object> queryWrapper = WrappersX.lambdaQueryJoin()
                 .selectAll(SysUser.class)
                 .selectAs(SysOrganization::getName,SysUserPageVO::getOrganizationName)
                 .leftJoin(SysOrganization.class,SysOrganization::getId,SysUser::getOrganizationId)
                 .eq(SysUser::getDeleted, GlobalConstants.NOT_DELETED_FLAG)
                 .likeIfPresent(SysUser::getUsername, qo.getUsername())
                 .likeIfPresent(SysUser::getEmail, qo.getEmail())
                 .likeIfPresent(SysUser::getPhone, qo.getPhone())
                 .likeIfPresent(SysUser::getNickname, qo.getNickname())
                 .eqIfPresent(SysUser::getStatus, qo.getStatus())
                 .eqIfPresent(SysUser::getSex, qo.getSex())
                 .eqIfPresent(SysUser::getType, qo.getType())
                 .inIfPresent(SysUser::getOrganizationId, qo.getOrganizationId())
                 .betweenIfPresent(SysUser::getCreateTime, qo.getStartTime(), qo.getEndTime());
         IPage<SysUserPageVO> iPage = this.selectJoinPage(page, SysUserPageVO.class, AUTO_RESULT_MAP, queryWrapper);
         return this.prodPage(iPage);
     }


    /**
     * 批量更新用户状态
     * @param userIds 用户ID集合
     * @param status 状态
     * @return 是否更新成功
     */
    default boolean updateStatusBatch(Collection<Long> userIds, Integer status) {
        int i = this.update(null, Wrappers.lambdaUpdate(SysUser.class)
                .set(SysUser::getStatus, status).in(SysUser::getUserId, userIds));
        return SqlHelper.retBool(i);
    }


    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 系统用户
     */
    default SysUser selectByUsername(String username) {
        return this.selectOne(WrappersX.<SysUser>lambdaQueryX().eq(SysUser::getUsername, username));
    }

    /**
     * 更新指定用户的密码
     * @param userId 用户
     * @param password 密码
     * @return 更新条数
     */
    default boolean updatePassword(Long userId, String password) {
        int update = this.update(null, Wrappers.<SysUser>lambdaUpdate()
                .eq(SysUser::getUserId, userId).set(SysUser::getPassword, password));
        return SqlHelper.retBool(update);
    }

    /**
     * 根据组织机构ID查询用户
     * @param organizationIds 组织机构id集合
     * @return 用户集合
     */
    default List<SysUser> listByOrganizationIds(Collection<Long> organizationIds) {
        return this.selectList(Wrappers.<SysUser>lambdaQuery().in(SysUser::getOrganizationId, organizationIds));
    }

    /**
     * 根据用户类型查询用户
     * @param userTypes 用户类型集合
     * @return 用户集合
     */
    default List<SysUser> listByUserTypes(Collection<Integer> userTypes) {
        return this.selectList(Wrappers.<SysUser>lambdaQuery().in(SysUser::getType, userTypes));
    }
    
    /**
     * 根据用户Id集合查询用户
     * @param userIds 用户Id集合
     * @return 用户集合
     */
    default List<SysUser> listByUserIds(Collection<Long> userIds) {
        return this.selectList(Wrappers.<SysUser>lambdaQuery().in(SysUser::getUserId, userIds));
    }

    /**
     * 根据RoleCode 查询对应用户
     * @param roleCodes 角色标识
     * @return List<SysUser> 该角色标识对应的用户列表
     */
    default List<SysUser> listByRoleCodes(Collection<String> roleCodes) {
        /*
        SELECT
		<include refid="Base_Alias_Column_List"/>
		FROM
		sys_user su
		WHERE
		EXISTS (
		    SELECT 1 from nk_sys_user_role ur
		    where
			ur.`role_code`
			in
			<foreach collection="roleCodes" item="roleCode" separator="," open="(" close=")">
				#{roleCode}
			</foreach>
			 and ur.user_id = su.user_id
		)
		AND su.deleted = 0
         */
        return this.selectJoinList(SysUser.class, AUTO_RESULT_MAP, WrappersX.lambdaQueryJoin()
                .selectAll(SysUser.class)
                .distinct()
                .leftJoin(SysUserRole.class, SysUserRole::getUserId, SysUser::getUserId)
                .in(SysUserRole::getRoleCode, roleCodes));
    }
    
    
    
    
    
    
    
}
