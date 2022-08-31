package com.schilings.neiko.admin.websocket.eventwebsokct;

import com.schilings.neiko.admin.websocket.jsonmessage.AnnouncementCloseMessage;
import com.schilings.neiko.common.util.json.JsonUtils;
import com.schilings.neiko.common.websocket.distribute.MessageDistributor;
import com.schilings.neiko.common.websocket.distribute.dto.MessageDTO;
import com.schilings.neiko.notify.event.announcement.AnnouncementCloseEvent;
import com.schilings.neiko.notify.event.StationNotifyPushEvent;
import com.schilings.neiko.notify.websocket.NotifyInfoDelegateHandler;
import com.schilings.neiko.notify.model.domain.NotifyInfo;
import com.schilings.neiko.system.model.entity.SysUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
public class NotifyWebsocketEventListener {

	private final MessageDistributor messageDistributor;

	private final NotifyInfoDelegateHandler<? super NotifyInfo> notifyInfoDelegateHandler;

	/**
	 * 公告关闭事件监听
	 * @param event the AnnouncementCloseEvent
	 */
	@Async
	@EventListener(AnnouncementCloseEvent.class)
	public void onAnnouncementCloseEvent(AnnouncementCloseEvent event) {
		// 构建公告关闭的消息体
		AnnouncementCloseMessage message = new AnnouncementCloseMessage();
		message.setId(event.getId());
		String msg = JsonUtils.toJson(message);

		// 广播公告关闭信息
		MessageDTO messageDO = new MessageDTO().setMessageText(msg).setNeedBroadcast(true);
		messageDistributor.distribute(messageDO);
	}

	/**
	 * 站内通知推送事件
	 * @param event the StationNotifyPushEvent
	 */
	@Async
	@EventListener(StationNotifyPushEvent.class)
	public void onAnnouncementPublishEvent(StationNotifyPushEvent event) {
		NotifyInfo notifyInfo = event.getNotifyInfo();
		List<SysUser> userList = event.getUserList();
		notifyInfoDelegateHandler.handle(userList, notifyInfo);
	}

}
