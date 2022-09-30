package com.schilings.neiko.system.service;

import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import com.schilings.neiko.system.model.dto.SysOrganizationDTO;
import com.schilings.neiko.system.model.entity.SysOrganization;
import com.schilings.neiko.system.model.qo.SysOrganizationQO;
import com.schilings.neiko.system.model.vo.SysOrganizationTree;

import java.util.List;

public interface SysOrganizationService extends ExtendService<SysOrganization> {

	/**
	 * 返回组织架构的树形结构
	 * @param sysOrganizationQO 组织机构查询条件
	 * @return OrganizationTree
	 */
	List<SysOrganizationTree> listTree(SysOrganizationQO sysOrganizationQO);

	/**
	 * 创建一个新的组织机构
	 * @param sysOrganizationDTO 组织机构DTO
	 * @return boolean 创建成功/失败
	 */
	boolean create(SysOrganizationDTO sysOrganizationDTO);

	/**
	 * 更新一个已有的组织机构
	 * @param sysOrganizationDTO 组织机构DTO
	 * @return boolean 更新成功/失败
	 */
	boolean update(SysOrganizationDTO sysOrganizationDTO);

	/**
	 * 根据组织ID 查询除该组织下的所有儿子组织
	 * @param organizationId 组织机构ID
	 * @return List<SysOrganization> 该组织的儿子组织
	 */
	List<SysOrganization> listSubOrganization(Long organizationId);

	/**
	 * 根据组织ID 查询除该组织下的所有孩子（子孙）组织
	 * @param organizationId 组织机构ID
	 * @return List<SysOrganization> 该组织的孩子组织
	 */
	List<SysOrganization> listChildOrganization(Long organizationId);

	/**
	 * 校正组织机构层级和深度
	 * @return 校正是否成功
	 */
	boolean revisedHierarchyAndPath();

}
