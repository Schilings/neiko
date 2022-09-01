package com.schilings.neiko.system.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 用户组织变更事件

 */
@Getter
@ToString
@RequiredArgsConstructor
public class UserOrganizationChangeEvent {

	private final Long userId;

	private final Long originOrganizationId;

	private final Long currentOrganizationId;

}
