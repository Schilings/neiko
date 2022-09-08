package com.schilings.neiko.extend.sa.token.oauth2.pojo;

@Deprecated
public interface RoleAuthority extends GrantedAuthority {

	@Override
	default String getAuthority() {
		return getRole();
	}

	String getRole();

}
