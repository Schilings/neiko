package com.schilings.neiko.notify.event;

import com.schilings.neiko.notify.model.domain.NotifyInfo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 通知发布事件,一旦该事件发布，就会向用户进行通知传递 通知方式包括短信、邮件、站内(websocket)等等
 * <p>
 * 不同的通知方式：见handler包
 * </p>
 */
@Getter
public class NotifyPublishEvent extends ApplicationEvent {

	/**
	 * 通知信息
	 */
	private final NotifyInfo notifyInfo;

	public NotifyPublishEvent(NotifyInfo notifyInfo) {
		super(notifyInfo);
		this.notifyInfo = notifyInfo;
	}

}
