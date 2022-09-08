package com.schilings.neiko.extend.sa.token.oauth2.event.authority;

import org.springframework.context.ApplicationEvent;

/**
 *
 * <p>
 * 权限更新事件
 * </p>
 *
 * @author Schilings
 */
public abstract class AuthorityEvent extends ApplicationEvent {

	public AuthorityEvent(Object source) {
		super(source);
	}

}
