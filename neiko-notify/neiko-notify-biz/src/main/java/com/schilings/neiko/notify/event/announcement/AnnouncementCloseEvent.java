package com.schilings.neiko.notify.event.announcement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 公告关闭事件
 */
@Getter
@ToString
@AllArgsConstructor
public class AnnouncementCloseEvent {

	/**
	 * ID
	 */
	private final Long id;

}
