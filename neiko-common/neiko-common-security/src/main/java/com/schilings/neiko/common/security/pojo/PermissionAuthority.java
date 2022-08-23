package com.schilings.neiko.common.security.pojo;

public interface PermissionAuthority extends GrantedAuthority {

    String getPermission();
}
