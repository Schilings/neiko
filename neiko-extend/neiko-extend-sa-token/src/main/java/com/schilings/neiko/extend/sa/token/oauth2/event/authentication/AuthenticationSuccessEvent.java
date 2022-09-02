package com.schilings.neiko.extend.sa.token.oauth2.event.authentication;

import com.schilings.neiko.extend.sa.token.oauth2.pojo.Authentication;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * <p>
 * 登录成功事件:指示成功认证的应用程序事件。
 * </p>
 *
 * @author Schilings
 */
@Setter
@Getter
public class AuthenticationSuccessEvent extends AbstractAuthenticationEvent {

	public AuthenticationSuccessEvent(Authentication source) {
		super(source);
	}

}
