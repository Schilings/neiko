package com.schilings.neiko.system.remote;

import com.schilings.neiko.common.core.http.RequestParameterObject;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.domain.SelectData;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.system.model.dto.SysRoleUpdateDTO;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.qo.RoleBindUserQO;
import com.schilings.neiko.system.model.qo.SysRoleQO;
import com.schilings.neiko.system.model.vo.RoleBindUserVO;
import com.schilings.neiko.system.model.vo.SysRolePageVO;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

import java.util.List;

@HttpExchange("/system/role")
public interface SysRoleRemote {

	/**
	 * 分页查询角色信息
	 * @param pageParam 分页参数
	 * @return PageResult 分页结果
	 */
	@GetExchange("/page")
	R<PageResult<SysRolePageVO>> getRolePage(PageParam pageParam, @RequestParameterObject SysRoleQO sysRoleQO);

	/**
	 * 通过ID查询角色信息
	 * @param id ID
	 * @return 角色信息
	 */
	@GetExchange("/{id}")
	R<SysRole> getById(@PathVariable("id") Long id);

	/**
	 * 新增系统角色表
	 * @param sysRole 系统角色表
	 * @return R
	 */
	@PutExchange
	R<Boolean> save(@RequestBody SysRole sysRole);

	/**
	 * 修改角色
	 * @param roleUpdateDTO 角色修改DTO
	 * @return success/false
	 */
	@PutExchange
	R<Boolean> update(@RequestBody SysRoleUpdateDTO roleUpdateDTO);

	/**
	 * 删除角色
	 * @param id id
	 * @return 结果信息
	 */
	@DeleteExchange("/{id}")
	R<Boolean> removeById(@PathVariable("id") Long id);

	/**
	 * 获取角色列表
	 * @return 角色列表
	 */
	@GetExchange("/list")
	R<List<SysRole>> listRoles();

	/**
	 * 更新角色权限
	 * @param roleCode 角色Code
	 * @param permissionIds 权限ID数组
	 * @return success、false
	 */
	@PutExchange("/permission/code/{roleCode}")
	R<Boolean> savePermissionIds(@PathVariable("roleCode") String roleCode, @RequestBody Long[] permissionIds);

	/**
	 * 返回角色的菜单集合
	 * @param roleCode 角色ID
	 * @return 属性集合
	 */
	@GetExchange("/permission/code/{roleCode}")
	R<List<Long>> getPermissionIds(@PathVariable("roleCode") String roleCode);

	/**
	 * 分页查询已授权指定角色的用户列表
	 * @param roleBindUserQO 角色绑定用户的查询条件
	 * @return R
	 */
	@GetExchange("/user/page")
	R<PageResult<RoleBindUserVO>> queryUserPageByRoleCode(PageParam pageParam,
			@RequestParameterObject RoleBindUserQO roleBindUserQO);

	/**
	 * 解绑与用户绑定关系
	 * @return R
	 */
	@DeleteExchange("/user")
	R<Boolean> unbindRoleUser(@RequestParam("userId") Long userId, @RequestParam("roleCode") String roleCode);

	/**
	 * 获取角色列表
	 * @return 角色列表
	 */
	@GetExchange("/select")
	R<List<SelectData>> listSelectData();

}
