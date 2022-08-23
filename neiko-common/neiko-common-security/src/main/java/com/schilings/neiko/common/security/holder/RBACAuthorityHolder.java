package com.schilings.neiko.common.security.holder;


import com.schilings.neiko.common.redis.RedisHelper;
import com.schilings.neiko.common.security.event.PermissionAuthorityChangedEvent;
import com.schilings.neiko.common.security.event.RoleAuthorityChangedEvent;
import com.schilings.neiko.common.security.properties.AuthPropertiesHolder;
import com.schilings.neiko.common.util.StringUtils;
import com.schilings.neiko.common.util.json.JsonUtils;
import com.schilings.neiko.common.util.json.TypeReference;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Set;
import java.util.function.Supplier;


public class RBACAuthorityHolder {


    private static final String ROLES = "auth:roles:";
    
    private static final String PERMISSIONS = "auth:permissions:";


    @ExceptionHandler
    public void refreshUserRole(RoleAuthorityChangedEvent event) {
        deleteRoles(event.getUserId());
    }

    @ExceptionHandler
    public void refreshRolePermission(PermissionAuthorityChangedEvent event) {
        deletePermissions(event.getRoleCode());
    }


    public static void deleteRoles(String userId) {
        RedisHelper.delete(ROLES + userId);
    }

    public static void deletePermissions(String roleCode) {
        RedisHelper.delete(PERMISSIONS + roleCode);
    }
    
    public static void setRoles(String userId, Set<String> roles) {
        RedisHelper.set(ROLES + userId, JsonUtils.toJson(roles),  AuthPropertiesHolder.authorityCacheTimeout());
    }

    public static void setPermissions(String roleCode, Set<String> permissions) {
        RedisHelper.set(PERMISSIONS + roleCode, JsonUtils.toJson(permissions),  AuthPropertiesHolder.authorityCacheTimeout());
    }

    /**
     * 获取Roles
     * @param userId
     * @param supplier
     * @return
     */
    public static Set<String> getRoles(String userId, Supplier<Set<String>> supplier) {
        String value = RedisHelper.get(ROLES + userId);
        if (StringUtils.isBlank(value)) {
            Set<String> roles = supplier.get();
            setRoles(userId, roles);
            return roles;
        }
        return JsonUtils.toObj(value, new TypeReference<Set<String>>() {});
    }

    /**
     * 获取Permissions
     * @param roleCode
     * @param supplier
     * @return
     */
    public static Set<String> getPermissions(String roleCode,Supplier<Set<String>> supplier) {
        String value = RedisHelper.get(PERMISSIONS + roleCode);
        if (StringUtils.isBlank(value)) {
            Set<String> permissions = supplier.get();
            setPermissions(roleCode, permissions);
            return permissions;
        }
        return JsonUtils.toObj(value, new TypeReference<Set<String>>() {});
    }
    
}
