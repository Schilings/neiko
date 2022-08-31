package com.schilings.neiko.notify.listener;


import com.schilings.neiko.notify.event.NotifyPublishEvent;
import com.schilings.neiko.notify.model.domain.NotifyInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 通知发布事件监听器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotifyPublishEventListener {

	private final NotifyPushExecutor notifyPushExecutor;

	/**
	 * 通知发布事件
	 * @param event the NotifyPublishEvent
	 */
	@Async
	@EventListener(NotifyPublishEvent.class)
	public void onNotifyPublishEvent(NotifyPublishEvent event) {
		NotifyInfo notifyInfo = event.getNotifyInfo();
		// 推送通知
		notifyPushExecutor.push(notifyInfo);
	}

}
