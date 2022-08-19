package com.schilings.neiko.system.converter;

import com.schilings.neiko.system.model.entity.SysMenu;
import com.schilings.neiko.system.model.vo.SysMenuPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface SysMenuConverter {

	SysMenuConverter INSTANCE = Mappers.getMapper(SysMenuConverter.class);

	/**
	 * PO 转 PageVO
	 * @param sysMenu 菜单权限实体
	 * @return SysMenuPageVO 菜单权限PageVO
	 */
	SysMenuPageVO poToPageVo(SysMenu sysMenu);



}
