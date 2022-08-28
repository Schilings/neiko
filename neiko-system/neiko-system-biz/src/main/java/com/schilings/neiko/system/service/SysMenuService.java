package com.schilings.neiko.system.service;

import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import com.schilings.neiko.system.model.dto.SysMenuCreateDTO;
import com.schilings.neiko.system.model.dto.SysMenuUpdateDTO;
import com.schilings.neiko.system.model.entity.SysMenu;
import com.schilings.neiko.system.model.qo.SysMenuQO;

import java.util.List;

public interface SysMenuService extends ExtendService<SysMenu> {

	/**
	 * 查询权限集合，并按sort排序（升序）
	 * @param sysMenuQO 查询条件
	 * @return List<SysMenu>
	 */
	List<SysMenu> listOrderBySort(SysMenuQO sysMenuQO);

	/**
	 * 更新菜单权限
	 * @param sysMenuUpdateDTO 菜单权限修改DTO
	 */
	void update(SysMenuUpdateDTO sysMenuUpdateDTO);

	/**
	 * 新建菜单权限
	 * @param sysMenuCreateDTO 菜单全新新建传输对象
	 * @return 新建成功返回 true
	 */
	boolean create(SysMenuCreateDTO sysMenuCreateDTO);

}
