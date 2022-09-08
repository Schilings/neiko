package com.schilings.neiko.extend.sa.token.oauth2.event.authority;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityInitEvent extends AuthorityEvent {

	private String loginType;

	private Object loginId;

	private String tokenValue;

	public AuthorityInitEvent(Object loginId, String loginType, String tokenValue) {
		super(loginId);
		this.loginId = loginId;
		this.loginType = loginType;
		this.tokenValue = tokenValue;
	}

	public AuthorityInitEvent(Object source) {
		super(source);
	}

}
