package com.schilings.neiko.notify.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 公告状态
 */
@Getter
@AllArgsConstructor
public enum AnnouncementStatusEnum {

	/**
	 * 关闭的
	 */
	DISABLED(0),

	/**
	 * 开启的
	 */
	ENABLED(1),

	/**
	 * 待发布
	 */
	UNPUBLISHED(2);

	private final int value;

}
