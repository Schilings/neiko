package com.schilings.neiko.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.schilings.neiko.common.log.operation.annotation.CreateOperationLogging;
import com.schilings.neiko.common.log.operation.annotation.DeleteOperationLogging;
import com.schilings.neiko.common.log.operation.annotation.UpdateOperationLogging;
import com.schilings.neiko.common.model.result.BaseResultCode;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.extend.sa.token.holder.RBACAuthorityHolder;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckPermission;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckScope;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.UserDetails;
import com.schilings.neiko.system.converter.SysMenuConverter;
import com.schilings.neiko.system.enums.SysMenuType;
import com.schilings.neiko.system.model.dto.SysMenuCreateDTO;
import com.schilings.neiko.system.model.dto.SysMenuUpdateDTO;
import com.schilings.neiko.system.model.entity.SysMenu;
import com.schilings.neiko.system.model.qo.SysMenuQO;
import com.schilings.neiko.system.model.vo.SysMenuGrantVO;
import com.schilings.neiko.system.model.vo.SysMenuPageVO;
import com.schilings.neiko.system.model.vo.SysMenuRouterVO;
import com.schilings.neiko.system.service.SysMenuService;
import com.schilings.neiko.system.service.SysRoleMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Oauth2CheckScope("system")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/menu")
@Tag(name = "菜单权限管理")
public class SysMenuController {

	private final SysMenuService sysMenuService;

	private final SysRoleMenuService sysRoleMenuService;

	/**
	 * 返回当前用户的路由集合
	 * @return 当前用户的路由
	 */
	@GetMapping("/router")
	@Operation(summary = "动态路由", description = "动态路由")
	public R<List<SysMenuRouterVO>> getUserPermission() {
		// 获取角色Code
		UserDetails user = RBACAuthorityHolder.getUserDetails();

		Collection<String> roleCodes = RBACAuthorityHolder.getRoles(user.getUserId());
		if (CollectionUtil.isEmpty(roleCodes)) {
			return R.ok(new ArrayList<>());
		}

		// 获取符合条件的权限
		Set<SysMenu> all = new HashSet<>();
		roleCodes.forEach(roleCode -> all.addAll(sysRoleMenuService.listMenus(roleCode)));

		// 筛选出菜单
		List<SysMenuRouterVO> menuVOList = all.stream()
				.filter(menuVo -> SysMenuType.BUTTON.getValue() != menuVo.getType())
				.sorted(Comparator.comparingInt(SysMenu::getSort)).map(SysMenuConverter.INSTANCE::poToRouterVo)
				.collect(Collectors.toList());

		return R.ok(menuVOList);
	}

	/**
	 * 查询菜单列表
	 * @param sysMenuQO 菜单权限查询对象
	 * @return R 通用返回体
	 */
	@GetMapping("/list")
	@Oauth2CheckPermission("@per.hasPermission('system:menu:read')")
	@Operation(summary = "查询菜单列表", description = "查询菜单列表")
	public R<List<SysMenuPageVO>> getSysMenuPage(SysMenuQO sysMenuQO) {
		List<SysMenu> sysMenus = sysMenuService.listOrderBySort(sysMenuQO);
		if (CollectionUtil.isEmpty(sysMenus)) {
			R.ok(new ArrayList<>());
		}
		List<SysMenuPageVO> voList = sysMenus.stream().map(SysMenuConverter.INSTANCE::poToPageVo)
				.collect(Collectors.toList());
		return R.ok(voList);
	}

	/**
	 * 查询授权菜单列表
	 * @return R 通用返回体
	 */
	@GetMapping("/grant-list")
	@Oauth2CheckPermission("@per.hasPermission('system:menu:read')")
	@Operation(summary = "查询授权菜单列表", description = "查询授权菜单列表")
	public R<List<SysMenuGrantVO>> getSysMenuGrantList() {
		List<SysMenu> sysMenus = sysMenuService.list();
		if (CollectionUtil.isEmpty(sysMenus)) {
			R.ok(new ArrayList<>());
		}
		List<SysMenuGrantVO> voList = sysMenus.stream().map(SysMenuConverter.INSTANCE::poToGrantVo)
				.collect(Collectors.toList());
		return R.ok(voList);
	}

	/**
	 * 新增菜单权限
	 * @param sysMenuCreateDTO 菜单权限
	 * @return R 通用返回体
	 */
	@CreateOperationLogging(msg = "新增菜单权限")
	@PostMapping
	@Oauth2CheckPermission("@per.hasPermission('system:menu:add')")
	@Operation(summary = "新增菜单权限", description = "新增菜单权限")
	public R<Void> save(@Valid @RequestBody SysMenuCreateDTO sysMenuCreateDTO) {
		return sysMenuService.create(sysMenuCreateDTO) ? R.ok()
				: R.fail(BaseResultCode.UPDATE_DATABASE_ERROR, "新增菜单权限失败");
	}

	/**
	 * 修改菜单权限
	 * @param sysMenuUpdateDTO 菜单权限修改DTO
	 * @return R 通用返回体
	 */
	@UpdateOperationLogging(msg = "修改菜单权限")
	@PutMapping
	@Oauth2CheckPermission("system:menu:edit")
	@Operation(summary = "修改菜单权限", description = "修改菜单权限")
	public R<Void> updateById(@RequestBody SysMenuUpdateDTO sysMenuUpdateDTO) {
		sysMenuService.update(sysMenuUpdateDTO);
		return R.ok();
	}

	/**
	 * 通过id删除菜单权限
	 * @param id id
	 * @return R 通用返回体
	 */
	@DeleteOperationLogging(msg = "通过id删除菜单权限")
	@DeleteMapping("/{id}")
	@Oauth2CheckPermission("system:menu:del")
	@Operation(summary = "通过id删除菜单权限", description = "通过id删除菜单权限")
	public R<Void> removeById(@PathVariable("id") Integer id) {
		return sysMenuService.removeById(id) ? R.ok() : R.fail(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除菜单权限失败");
	}

}
