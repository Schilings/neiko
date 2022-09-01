package com.schilings.neiko.system.event;


import com.schilings.neiko.system.model.entity.SysUser;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * 用户创建事件

 */
@Getter
@ToString
public class UserCreatedEvent {

	private final SysUser sysUser;

	private final List<String> roleCodes;

	public UserCreatedEvent(SysUser sysUser, List<String> roleCodes) {
		this.sysUser = sysUser;
		this.roleCodes = roleCodes;
	}

}
