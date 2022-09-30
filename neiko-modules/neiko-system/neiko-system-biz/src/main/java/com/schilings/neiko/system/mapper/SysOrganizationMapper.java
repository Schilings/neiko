package com.schilings.neiko.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.schilings.neiko.common.model.constants.GlobalConstants;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.system.model.dto.OrganizationMoveChildParam;
import com.schilings.neiko.system.model.entity.SysOrganization;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;

import java.util.List;

public interface SysOrganizationMapper extends ExtendMapper<SysOrganization> {

	/**
	 * 根据组织ID 查询除该组织下的所有儿子组织
	 * @param organizationId 组织机构ID
	 * @return List<SysOrganization> 该组织的儿子组织
	 */
	default List<SysOrganization> listSubOrganization(Long organizationId) {
		LambdaQueryWrapper<SysOrganization> wrapper = Wrappers.<SysOrganization>lambdaQuery()
				.eq(SysOrganization::getDeleted, GlobalConstants.NOT_DELETED_FLAG)
				.eq(SysOrganization::getParentId, organizationId);
		return this.selectList(wrapper);
	}

	/**
	 * 跟随父节点移动子节点
	 * @param param OrganizationMoveChildParam 跟随移动子节点的参数对象
	 */
	void followMoveChildNode(@Param("param") OrganizationMoveChildParam param);

	/**
	 * 根据组织机构Id，查询该组织下的所有子部门,包括子部门的子部门
	 * @param organizationId 组织机构ID
	 * @return 子部门集合
	 */
	List<SysOrganization> listChildOrganization(Long organizationId);

	/**
	 * 检查指定机构下是否存在子机构,包括子部门的子部门
	 * @param organizationId 机构id
	 * @return 存在返回 true
	 */
	@Nullable
	Boolean existsChildOrganization(Long organizationId);

	/**
	 * 批量更新节点层级和深度
	 * @param depth 深度
	 * @param hierarchy 层级
	 * @param organizationIds 组织id集合
	 */
	default boolean updateHierarchyAndPathBatch(int depth, String hierarchy, List<Long> organizationIds) {
		LambdaUpdateWrapper<SysOrganization> wrapper = Wrappers.lambdaUpdate(SysOrganization.class)
				.set(SysOrganization::getDepth, depth).set(SysOrganization::getHierarchy, hierarchy)
				.in(SysOrganization::getId, organizationIds);
		int update = this.update(null, wrapper);
		return SqlHelper.retBool(update);

	}

}
