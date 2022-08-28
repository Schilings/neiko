package com.schilings.neiko.system.converter;

import com.schilings.neiko.system.model.dto.SysMenuCreateDTO;
import com.schilings.neiko.system.model.dto.SysMenuUpdateDTO;
import com.schilings.neiko.system.model.entity.SysMenu;
import com.schilings.neiko.system.model.vo.SysMenuGrantVO;
import com.schilings.neiko.system.model.vo.SysMenuPageVO;
import com.schilings.neiko.system.model.vo.SysMenuRouterVO;
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

	/**
	 * PO 转 GrantVo
	 * @param sysMenu 菜单权限实体
	 * @return SysMenuPageVO 菜单权限GrantVO
	 */
	SysMenuGrantVO poToGrantVo(SysMenu sysMenu);

	/**
	 * PO 转 VO
	 * @param sysMenu 菜单权限实体
	 * @return SysMenuVO
	 */
	SysMenuRouterVO poToRouterVo(SysMenu sysMenu);

	/**
	 * createDto 转 Po
	 * @param sysMenuCreateDTO 菜单新建对象
	 * @return SysMenu 菜单权限的持久化对象
	 */
	SysMenu createDtoToPo(SysMenuCreateDTO sysMenuCreateDTO);

	/**
	 * updateDto 转 Po
	 * @param sysMenuUpdateDTO 菜单修改对象
	 * @return SysMenu 菜单权限的持久化对象
	 */
	SysMenu updateDtoToPo(SysMenuUpdateDTO sysMenuUpdateDTO);

}
