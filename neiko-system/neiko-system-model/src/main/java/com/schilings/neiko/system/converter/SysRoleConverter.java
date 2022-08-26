package com.schilings.neiko.system.converter;

import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.vo.SysRolePageVO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SysRoleConverter {

	SysRoleConverter INSTANCE = Mappers.getMapper(SysRoleConverter.class);

	/**
	 * PO 转 PageVO
	 * @param sysRole 系统角色
	 * @return SysRolePageVO 系统角色分页VO
	 */
	SysRolePageVO poToPageVo(SysRole sysRole);

}
