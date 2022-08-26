package com.schilings.neiko.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.schilings.neiko.common.core.validation.group.CreateGroup;
import com.schilings.neiko.common.log.operation.annotation.CreateOperationLogging;
import com.schilings.neiko.common.log.operation.annotation.ReadOperationLogging;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.BaseResultCode;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.common.model.result.SystemResultCode;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckPermission;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckScope;
import com.schilings.neiko.system.converter.SysUserConverter;
import com.schilings.neiko.system.model.dto.SysUserDTO;
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

import javax.validation.groups.Default;
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
	@ReadOperationLogging(msg = "分页查询系统用户")
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

		// 明文密码
		// String rawPassword = passwordHelper.decodeAes(sysUserDTO.getPass());
		// sysUserDTO.setPassword(rawPassword);
		//
		// // 密码规则校验
		// if (passwordHelper.validateRule(rawPassword)) {
		// return sysUserService.addSysUser(sysUserDTO) ? R.ok()
		// : R.fail(BaseResultCode.UPDATE_DATABASE_ERROR, "新增系统用户失败");
		// }
		else {
			return R.fail(SystemResultCode.BAD_REQUEST, "密码格式不符合规则!");
		}
	}

}
