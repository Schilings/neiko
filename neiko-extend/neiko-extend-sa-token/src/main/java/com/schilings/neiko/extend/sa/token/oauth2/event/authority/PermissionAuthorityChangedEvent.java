package com.schilings.neiko.extend.sa.token.oauth2.event.authority;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 *
 * <p>
 * 角色权限更新事件
 * </p>
 *
 * @author Schilings
 */
@Getter
@Setter
public class PermissionAuthorityChangedEvent extends AuthorityEvent {

	private final Collection<String> roleCode;

	public PermissionAuthorityChangedEvent(Collection<String> roleCode) {
		super(roleCode);
		this.roleCode = roleCode;
	}

}
