package com.schilings.neiko.system.remote;

import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.system.model.dto.SysMenuCreateDTO;
import com.schilings.neiko.system.model.dto.SysMenuUpdateDTO;
import com.schilings.neiko.system.model.qo.SysMenuQO;
import com.schilings.neiko.system.model.vo.SysMenuGrantVO;
import com.schilings.neiko.system.model.vo.SysMenuPageVO;
import com.schilings.neiko.system.model.vo.SysMenuRouterVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;

import java.util.*;

@HttpExchange("/system/menu")
public interface SysMenuRemote {

	/**
	 * 返回当前用户的路由集合
	 * @return 当前用户的路由
	 */
	@GetExchange("/router")
	R<List<SysMenuRouterVO>> getUserPermission();

	/**
	 * 查询菜单列表
	 * @param sysMenuQO 菜单权限查询对象
	 * @return R 通用返回体
	 */
	@GetExchange("/list")
	R<List<SysMenuPageVO>> getSysMenuPage(SysMenuQO sysMenuQO);

	/**
	 * 查询授权菜单列表
	 * @return R 通用返回体
	 */
	@GetExchange("/grantList")
	R<List<SysMenuGrantVO>> getSysMenuGrantList();

	/**
	 * 新增菜单权限
	 * @param sysMenuCreateDTO 菜单权限
	 * @return R 通用返回体
	 */
	@PostExchange
	R<Void> save(@RequestBody SysMenuCreateDTO sysMenuCreateDTO);

	/**
	 * 修改菜单权限
	 * @param sysMenuUpdateDTO 菜单权限修改DTO
	 * @return R 通用返回体
	 */
	@PutExchange
	R<Void> updateById(@RequestBody SysMenuUpdateDTO sysMenuUpdateDTO);

	/**
	 * 通过id删除菜单权限
	 * @param id id
	 * @return R 通用返回体
	 */
	@DeleteExchange("/{id}")
	R<Void> removeById(@PathVariable("id") Long id);

}
