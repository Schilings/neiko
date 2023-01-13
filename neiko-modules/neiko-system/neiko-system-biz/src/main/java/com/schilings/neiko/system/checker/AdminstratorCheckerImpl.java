package com.schilings.neiko.system.checker;

import com.schilings.neiko.authorization.common.util.SecurityUtils;
import com.schilings.neiko.common.util.StringUtils;
import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.properties.SystemProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * 超级管理员账户规则配置
 */
@Service
@RequiredArgsConstructor
public class AdminstratorCheckerImpl implements AdminstratorChecker {

	private final SystemProperties systemProperties;

	/**
	 * 校验用户是否为超级管理员
	 * @return boolean
	 */
	@Override
	public boolean isAdminstrator(SysUser sysUser) {
		SystemProperties.Administrator administrator = systemProperties.getAdministrator();
		// 优先匹配UserId
		if (administrator.getUserId().equals(sysUser.getUserId())) {
			return true;
		}
		// 再匹配Username
		return StringUtils.isNotEmpty(administrator.getUsername())
				&& administrator.getUsername().equals(sysUser.getUsername());
	}

	/**
	 * 修改权限校验
	 * @param targetUser 目标用户
	 * @return 是否有权限修改目标用户
	 */
	@Override
	public boolean hasModifyPermission(SysUser targetUser) {
		// 如果需要修改的用户是超级管理员，则只能本人修改
		if (this.isAdminstrator(targetUser)) {
			return SecurityUtils.getUser().getUsername().equals(targetUser.getUsername());
		}
		return true;
	}

}
