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
public abstract class AuthorityChangedEvent extends ApplicationEvent {

	public AuthorityChangedEvent(Object source) {
		super(source);
	}

}
