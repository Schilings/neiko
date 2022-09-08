package com.schilings.neiko.extend.sa.token.oauth2.pojo;

@Deprecated
public interface PermissionAuthority extends GrantedAuthority {

	@Override
	default String getAuthority() {
		return getPermission();
	}

	String getPermission();

}
