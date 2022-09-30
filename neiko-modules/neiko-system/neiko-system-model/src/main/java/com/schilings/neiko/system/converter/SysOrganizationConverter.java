package com.schilings.neiko.system.converter;

import com.schilings.neiko.system.model.dto.SysOrganizationDTO;
import com.schilings.neiko.system.model.entity.SysOrganization;
import com.schilings.neiko.system.model.vo.SysOrganizationTree;
import com.schilings.neiko.system.model.vo.SysOrganizationVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SysOrganizationConverter {

	SysOrganizationConverter INSTANCE = Mappers.getMapper(SysOrganizationConverter.class);

	/**
	 * 实体转树节点实体
	 * @param sysOrganization 组织架构实体
	 * @return 组织架构树类型
	 */
	SysOrganizationTree poToTree(SysOrganization sysOrganization);

	/**
	 * 实体转组织架构VO
	 * @param sysOrganization 组织架构实体
	 * @return SysOrganizationVO
	 */
	SysOrganizationVO poToVo(SysOrganization sysOrganization);

	/**
	 * 新增传输对象转实体
	 * @param sysOrganizationDTO 组织机构DTO
	 * @return 组织机构实体
	 */
	SysOrganization dtoToPo(SysOrganizationDTO sysOrganizationDTO);

}
