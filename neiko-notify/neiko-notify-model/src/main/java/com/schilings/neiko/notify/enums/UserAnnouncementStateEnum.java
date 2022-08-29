package com.schilings.neiko.notify.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户公告状态
 */
@Getter
@AllArgsConstructor
public enum UserAnnouncementStateEnum {

	/**
	 * 未读
	 */
	UNREAD(0),
	/**
	 * 已读
	 */
	READ(1);

	private final int value;

}
