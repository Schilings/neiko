package com.schilings.neiko.admin.datascope.component;


import com.schilings.neiko.extend.sa.token.oauth2.pojo.RoleAuthority;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.entity.SysUser;

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
