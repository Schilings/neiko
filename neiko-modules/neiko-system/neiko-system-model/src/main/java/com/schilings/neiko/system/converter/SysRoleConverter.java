package com.schilings.neiko.system.converter;

import com.schilings.neiko.system.model.dto.SysRoleUpdateDTO;
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

	/**
	 * 修改DTO 转 PO
	 * @param dto 修改DTO
	 * @return SysRole PO
	 */
	SysRole dtoToPo(SysRoleUpdateDTO dto);

}
