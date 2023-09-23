package com.schilings.neiko.system.controller;

import com.schilings.neiko.common.log.operation.annotation.CreateOperationLogging;
import com.schilings.neiko.common.log.operation.annotation.DeleteOperationLogging;
import com.schilings.neiko.common.log.operation.annotation.UpdateOperationLogging;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.domain.SelectData;
import com.schilings.neiko.common.model.result.BaseResultCode;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.system.constant.SysRoleConst;
import com.schilings.neiko.system.converter.SysRoleConverter;
import com.schilings.neiko.system.model.dto.SysRoleUpdateDTO;
import com.schilings.neiko.system.model.entity.SysMenu;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.qo.RoleBindUserQO;
import com.schilings.neiko.system.model.qo.SysRoleQO;
import com.schilings.neiko.system.model.vo.RoleBindUserVO;
import com.schilings.neiko.system.model.vo.SysRolePageVO;
import com.schilings.neiko.system.service.SysRoleMenuService;
import com.schilings.neiko.system.service.SysRoleService;
import com.schilings.neiko.system.service.SysUserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
@Tag(name = "角色管理模块")
public class SysRoleController {

	private final SysRoleService sysRoleService;

	private final SysUserRoleService sysUserRoleService;

	private final SysRoleMenuService sysRoleMenuService;

	/**
	 * 分页查询角色信息
	 * @param pageParam 分页参数
	 * @return PageResult 分页结果
	 */
	@GetMapping("/page")
	@Operation(summary = "分页查询系统角色")
	@PreAuthorize(value = "hasAuthority('system:role:read')")
	public R<PageResult<SysRolePageVO>> getRolePage(@Validated PageParam pageParam, SysRoleQO sysRoleQO) {
		return R.ok(sysRoleService.queryPage(pageParam, sysRoleQO));
	}

	/**
	 * 通过ID查询角色信息
	 * @param id ID
	 * @return 角色信息
	 */
	@GetMapping("/{id}")
	@PreAuthorize(value = "hasAuthority('system:role:read')")
	public R<SysRole> getById(@PathVariable("id") Long id) {
		return R.ok(sysRoleService.getById(id));
	}

	/**
	 * 新增系统角色表
	 * @param sysRole 系统角色表
	 * @return R
	 */
	@CreateOperationLogging(msg = "新增系统角色")
	@PostMapping
	@PreAuthorize(value = "hasAuthority('system:role:add')")
	@Operation(summary = "新增系统角色", description = "新增系统角色")
	public R<Boolean> save(@Valid @RequestBody SysRole sysRole) {
		return R.ok(sysRoleService.save(sysRole));
	}

	/**
	 * 修改角色
	 * @param roleUpdateDTO 角色修改DTO
	 * @return success/false
	 */
	@UpdateOperationLogging(msg = "修改系统角色")
	@PutMapping
	@PreAuthorize(value = "hasAuthority('system:role:edit')")
	@Operation(summary = "修改系统角色", description = "修改系统角色")
	public R<Boolean> update(@Valid @RequestBody SysRoleUpdateDTO roleUpdateDTO) {
		SysRole sysRole = SysRoleConverter.INSTANCE.dtoToPo(roleUpdateDTO);
		return R.ok(sysRoleService.updateById(sysRole));
	}

	/**
	 * 删除角色
	 * @param id id
	 * @return 结果信息
	 */
	@DeleteMapping("/{id}")
	@DeleteOperationLogging(msg = "通过id删除系统角色")
	@PreAuthorize(value = "hasAuthority('system:role:del')")
	@Operation(summary = "通过id删除系统角色", description = "通过id删除系统角色")
	public R<Boolean> removeById(@PathVariable("id") Long id) {
		SysRole oldRole = sysRoleService.getById(id);
		if (SysRoleConst.Type.SYSTEM.getValue().equals(oldRole.getType())) {
			return R.fail(BaseResultCode.LOGIC_CHECK_ERROR, "系统角色不允许被删除!");
		}
		return R.ok(sysRoleService.removeById(id));
	}

	/**
	 * 获取角色列表
	 * @return 角色列表
	 */
	@GetMapping("/list")
	public R<List<SysRole>> listRoles() {
		return R.ok(sysRoleService.list());
	}

	/**
	 * 更新角色权限
	 * @param roleCode 角色Code
	 * @param permissionIds 权限ID数组
	 * @return success、false
	 */
	@PutMapping("/permission/code/{roleCode}")
	@UpdateOperationLogging(msg = "更新角色权限")
	@PreAuthorize(value = "hasAuthority('system:role:grant')")
	@Operation(summary = "更新角色权限", description = "更新角色权限")
	public R<Boolean> savePermissionIds(@PathVariable("roleCode") String roleCode, @RequestBody Long[] permissionIds) {
		return R.ok(sysRoleMenuService.saveRoleMenus(roleCode, permissionIds));
	}

	/**
	 * 返回角色的菜单集合
	 * @param roleCode 角色ID
	 * @return 属性集合
	 */
	@GetMapping("/permission/code/{roleCode}")
	public R<List<Long>> getPermissionIds(@PathVariable("roleCode") String roleCode) {
		List<SysMenu> sysMenus = sysRoleMenuService.listMenus(roleCode);
		List<Long> menuIds = sysMenus.stream().map(SysMenu::getId).collect(Collectors.toList());
		return R.ok(menuIds);
	}

	/**
	 * 分页查询已授权指定角色的用户列表
	 * @param roleBindUserQO 角色绑定用户的查询条件
	 * @return R
	 */
	@GetMapping("/user/page")
	@PreAuthorize(value = "hasAuthority('system:role:grant')")
	@Operation(summary = "查看已授权指定角色的用户列表", description = "查看已授权指定角色的用户列表")
	public R<PageResult<RoleBindUserVO>> queryUserPageByRoleCode(PageParam pageParam,
			@Valid RoleBindUserQO roleBindUserQO) {
		return R.ok(sysUserRoleService.queryUserPage(pageParam, roleBindUserQO));
	}

	/**
	 * 解绑与用户绑定关系
	 * @return R
	 */
	@DeleteMapping("/user")
	@PreAuthorize(value = "hasAuthority('system:role:grant')")
	@Operation(summary = "解绑与用户绑定关系", description = "解绑与用户绑定关系")
	public R<Boolean> unbindRoleUser(@RequestParam("userId") Long userId, @RequestParam("roleCode") String roleCode) {
		return R.ok(sysUserRoleService.unbindRoleUser(userId, roleCode));
	}

	/**
	 * 获取角色列表
	 * @return 角色列表
	 */
	@GetMapping("/select")
	public R<List<SelectData>> listSelectData() {
		return R.ok(sysRoleService.listSelectData());
	}

}
