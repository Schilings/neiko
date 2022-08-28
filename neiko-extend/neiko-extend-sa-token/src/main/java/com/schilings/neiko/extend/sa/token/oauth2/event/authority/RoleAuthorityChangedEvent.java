package com.schilings.neiko.extend.sa.token.oauth2.event.authority;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 *
 * <p>
 * 用户角色更新事件
 * </p>
 *
 * @author Schilings
 */
@Getter
@Setter
public class RoleAuthorityChangedEvent extends AuthorityChangedEvent {

	private final Collection<String> userId;

	public RoleAuthorityChangedEvent(Collection<String> userId) {
		super(userId);
		this.userId = userId;
	}

}
