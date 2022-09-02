package com.schilings.neiko.extend.sa.token.oauth2.event.authentication;

import com.schilings.neiko.extend.sa.token.oauth2.pojo.Authentication;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * <p>
 * 登出事件:指示成功注销的应用程序事件
 * </p>
 *
 * @author Schilings
 */
@Getter
@Setter
public class LogoutSuccessEvent extends AbstractAuthenticationEvent {

	public LogoutSuccessEvent(Authentication source) {
		super(source);
	}

}
