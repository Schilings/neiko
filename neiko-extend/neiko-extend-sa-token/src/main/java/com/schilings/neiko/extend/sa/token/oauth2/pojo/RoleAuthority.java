package com.schilings.neiko.extend.sa.token.oauth2.pojo;

public interface RoleAuthority extends GrantedAuthority {

	@Override
	default String getAuthority() {
		return getRole();
	}

	String getRole();

}
