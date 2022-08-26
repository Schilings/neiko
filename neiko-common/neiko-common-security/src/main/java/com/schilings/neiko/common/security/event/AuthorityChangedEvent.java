package com.schilings.neiko.common.security.event;

import org.springframework.context.ApplicationEvent;

/**
 *
 * <p>
 * 权限更新事件
 * </p>
 *
 * @author Schilings
 */
public class AuthorityChangedEvent extends ApplicationEvent {

	public AuthorityChangedEvent(Object source) {
		super(source);
	}

}
