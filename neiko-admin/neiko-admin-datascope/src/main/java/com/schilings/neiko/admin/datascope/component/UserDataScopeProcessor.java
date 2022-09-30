package com.schilings.neiko.admin.datascope.component;

import java.util.Collection;

public interface UserDataScopeProcessor {

	/**
	 * 根据用户和角色信息，合并用户最终的数据权限
	 * @param user 用户
	 * @param roles 角色列表
	 * @return UserDataScope
	 */
	UserDataScope mergeScopeType(Long userId, Collection<String> roles);

}
