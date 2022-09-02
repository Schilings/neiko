package com.schilings.neiko.notify.websocket.announcement;

import com.schilings.neiko.common.websocket.message.JsonWebSocketMessage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 公告发布消息
 *
 */
@Getter
@Setter
public class AnnouncementPushMessage extends JsonWebSocketMessage {

	public AnnouncementPushMessage() {
		super("announcement-push");
	}

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 永久有效的
	 */
	private Integer immortal;

	/**
	 * 截止日期
	 */
	private LocalDateTime deadline;

}
