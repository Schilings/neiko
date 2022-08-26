package com.schilings.neiko.system.checker;

import com.schilings.neiko.extend.sa.token.oauth2.pojo.UserDetails;
import com.schilings.neiko.system.model.entity.SysUser;

/**
 * 超级管理员账户规则配置
 *
 * @author lingting 2020-06-24 21:00:15
 */
public interface AdminstratorChecker {

	/**
	 * 校验用户是否为超级管理员
	 * @return boolean
	 */
	boolean isAdminstrator(SysUser sysUser);

	/**
	 * 修改权限校验
	 * @param targetUser 目标用户
	 * @return 是否有权限修改目标用户
	 */
	boolean hasModifyPermission(SysUser targetUser);

}
