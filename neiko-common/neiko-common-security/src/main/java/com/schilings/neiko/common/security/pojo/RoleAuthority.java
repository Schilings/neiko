package com.schilings.neiko.common.security.pojo;

public interface RoleAuthority extends GrantedAuthority {

    String getRole();
}
