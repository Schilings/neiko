package com.schilings.neiko.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.schilings.neiko.common.core.validation.group.CreateGroup;
import com.schilings.neiko.common.core.validation.group.UpdateGroup;
import com.schilings.neiko.common.log.operation.annotation.CreateOperationLogging;
import com.schilings.neiko.common.log.operation.annotation.DeleteOperationLogging;
import com.schilings.neiko.common.log.operation.annotation.ReadOperationLogging;
import com.schilings.neiko.common.log.operation.annotation.UpdateOperationLogging;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.BaseResultCode;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.common.model.result.SystemResultCode;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckPermission;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckScope;
import com.schilings.neiko.system.constant.SysUserConst;
import com.schilings.neiko.system.converter.SysUserConverter;
import com.schilings.neiko.system.model.dto.SysUserDTO;
import com.schilings.neiko.system.model.dto.SysUserPassDTO;
import com.schilings.neiko.system.model.dto.SysUserScope;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.model.qo.SysUserQO;
import com.schilings.neiko.system.model.vo.SysUserInfo;
import com.schilings.neiko.system.model.vo.SysUserPageVO;
import com.schilings.neiko.system.security.PasswordHelper;
import com.schilings.neiko.system.service.SysUserRoleService;
import com.schilings.neiko.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Oauth2CheckScope("system")
@Slf4j
@Validated
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
@Tag(name = "用户管理模块")
public class SysUserController {

	private final SysUserService sysUserService;

	private final SysUserRoleService sysUserRoleService;

	private final PasswordHelper passwordHelper;

	/**
	 * 分页查询用户
	 * @param pageParam 参数集
	 * @return 用户集合
	 */
	@GetMapping("/page")
	@Oauth2CheckPermission("system:user:read")
	@Operation(summary = "分页查询系统用户")
	public R<PageResult<SysUserPageVO>> getUserPage(@Validated PageParam pageParam, SysUserQO sysUserQO) {
		return R.ok(sysUserService.queryPage(pageParam, sysUserQO));
	}

	/**
	 * 获取指定用户的基本信息
	 * @param userId 用户ID
	 * @return SysUserInfo
	 */
	@GetMapping("/{userId}")
	@Oauth2CheckPermission("system:user:read")
	@Operation(summary = "获取指定用户的基本信息")
	public R<SysUserInfo> getSysUserInfo(@PathVariable("userId") Integer userId) {
		SysUser sysUser = sysUserService.getById(userId);
		if (sysUser == null) {
			return R.ok();
		}
		SysUserInfo sysUserInfo = SysUserConverter.INSTANCE.poToInfo(sysUser);
		return R.ok(sysUserInfo);
	}

	/**
	 * 获取用户 所拥有的角色ID
	 * @param userId userId
	 */
	@GetMapping("/scope/{userId}")
	@Oauth2CheckPermission("system:user:grant")
	@Operation(summary = "获取用户所拥有的角色ID")
	public R<SysUserScope> getUserRoleIds(@PathVariable("userId") Long userId) {

		List<SysRole> roleList = sysUserRoleService.listRoles(userId);

		List<String> roleCodes = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(roleList)) {
			roleList.forEach(role -> roleCodes.add(role.getCode()));
		}
		SysUserScope sysUserScope = new SysUserScope();
		sysUserScope.setRoleCodes(roleCodes);
		return R.ok(sysUserScope);
	}

	/**
	 * 新增用户
	 * @param sysUserDTO userInfo
	 * @return success/false
	 */
	@PostMapping
	@CreateOperationLogging(msg = "新增系统用户")
	@Oauth2CheckPermission("system:user:add")
	@Operation(summary = "新增系统用户", description = "新增系统用户")
	public R<Void> addSysUser(@Validated({ Default.class, CreateGroup.class }) @RequestBody SysUserDTO sysUserDTO) {
		SysUser user = sysUserService.getByUsername(sysUserDTO.getUsername());
		if (user != null) {
			return R.fail(BaseResultCode.LOGIC_CHECK_ERROR, "用户名已存在");
		}

		// 密码规则校验
		if (passwordHelper.validateRule(sysUserDTO.getPass())) {
			return sysUserService.addSysUser(sysUserDTO) ? R.ok()
					: R.fail(BaseResultCode.UPDATE_DATABASE_ERROR, "新增系统用户失败");
		}
		else {
			return R.fail(SystemResultCode.BAD_REQUEST, "密码格式不符合规则!");
		}
	}

	/**
	 * 修改用户个人信息
	 * @param sysUserDto userInfo
	 * @return success/false
	 */
	@PutMapping
	@UpdateOperationLogging(msg = "修改系统用户")
	@Oauth2CheckPermission("@per.hasPermission('system:user:edit')")
	@Operation(summary = "修改系统用户", description = "修改系统用户")
	public R<Void> updateUserInfo(@Validated({ Default.class, UpdateGroup.class }) @RequestBody SysUserDTO sysUserDto) {
		return sysUserService.updateSysUser(sysUserDto) ? R.ok()
				: R.fail(BaseResultCode.UPDATE_DATABASE_ERROR, "修改系统用户失败");
	}

	/**
	 * 删除用户信息
	 */
	@DeleteMapping("/{userId}")
	@DeleteOperationLogging(msg = "通过id删除系统用户")
	@Oauth2CheckPermission("@per.hasPermission('system:user:del')")
	@Operation(summary = "通过id删除系统用户", description = "通过id删除系统用户")
	public R<Void> deleteByUserId(@PathVariable("userId") Long userId) {
		return sysUserService.deleteByUserId(userId) ? R.ok()
				: R.fail(BaseResultCode.UPDATE_DATABASE_ERROR, "删除系统用户失败");
	}

	/**
	 * 修改用户权限信息 比如角色 数据权限等
	 * @param sysUserScope sysUserScope
	 * @return success/false
	 */
	@PutMapping("/scope/{userId}")
	@UpdateOperationLogging(msg = "系统用户授权")
	@Oauth2CheckPermission("@per.hasPermission('system:user:grant')")
	@Operation(summary = "系统用户授权", description = "系统用户授权")
	public R<Void> updateUserScope(@PathVariable("userId") Long userId, @RequestBody SysUserScope sysUserScope) {
		return sysUserService.updateUserScope(userId, sysUserScope) ? R.ok()
				: R.fail(BaseResultCode.UPDATE_DATABASE_ERROR, "系统用户授权失败");
	}

	/**
	 * 修改用户密码
	 */
	@PutMapping("/pass/{userId}")
	@UpdateOperationLogging(msg = "修改系统用户密码")
	@Oauth2CheckPermission("@per.hasPermission('system:user:pass')")
	@Operation(summary = "修改系统用户密码", description = "修改系统用户密码")
	public R<Void> updateUserPass(@PathVariable("userId") Long userId, @RequestBody SysUserPassDTO sysUserPassDTO) {
		String pass = sysUserPassDTO.getPass();
		if (!pass.equals(sysUserPassDTO.getConfirmPass())) {
			return R.fail(SystemResultCode.BAD_REQUEST, "两次密码输入不一致!");
		}

		// 解密明文密码
		String rawPassword = passwordHelper.decodeAes(pass);
		// 密码规则校验
		if (passwordHelper.validateRule(rawPassword)) {
			return sysUserService.updatePassword(userId, rawPassword) ? R.ok()
					: R.fail(BaseResultCode.UPDATE_DATABASE_ERROR, "修改用户密码失败！");
		}
		else {
			return R.fail(SystemResultCode.BAD_REQUEST, "密码格式不符合规则!");
		}
	}

	/**
	 * 批量修改用户状态
	 */
	@PutMapping("/status")
	@UpdateOperationLogging(msg = "批量修改用户状态")
	@Oauth2CheckPermission("system:user:edit")
	@Operation(summary = "批量修改用户状态", description = "批量修改用户状态")
	public R<Void> updateUserStatus(@NotEmpty(message = "用户ID不能为空") @RequestBody List<Long> userIds,
			@NotNull(message = "用户状态不能为空") @RequestParam("status") Integer status) {

		if (!SysUserConst.Status.NORMAL.getValue().equals(status)
				&& !SysUserConst.Status.LOCKED.getValue().equals(status)) {
			throw new ValidationException("不支持的用户状态！");
		}
		return sysUserService.updateUserStatusBatch(userIds, status) ? R.ok()
				: R.fail(BaseResultCode.UPDATE_DATABASE_ERROR, "批量修改用户状态！");
	}

	@UpdateOperationLogging(msg = "修改系统用户头像")
	@Oauth2CheckPermission("system:user:edit")
	@PostMapping("/avatar")
	@Operation(summary = "修改系统用户头像", description = "修改系统用户头像")
	public R<String> updateAvatar(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) {
		String objectName;
		try {
			objectName = sysUserService.updateAvatar(file, userId);
		}
		catch (IOException e) {
			log.error("修改系统用户头像异常", e);
			return R.result(BaseResultCode.FILE_UPLOAD_ERROR, null);
		}
		return R.ok(objectName);
	}

}
