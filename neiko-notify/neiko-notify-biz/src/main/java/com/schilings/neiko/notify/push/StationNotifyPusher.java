package com.schilings.neiko.notify.push;

import com.schilings.neiko.notify.enums.NotifyChannelEnum;
import com.schilings.neiko.notify.event.StationNotifyPushEvent;
import com.schilings.neiko.notify.model.domain.NotifyInfo;
import com.schilings.neiko.system.model.entity.SysUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息通知站内推送
 */
@Component
@RequiredArgsConstructor
public class StationNotifyPusher implements NotifyPusher {

	private final ApplicationEventPublisher publisher;

	/**
	 * 当前发布者对应的接收方式
	 * @see NotifyChannelEnum
	 * @return 推送方式
	 */
	@Override
	public Integer notifyChannel() {
		return NotifyChannelEnum.STATION.getValue();
	}

	@Override
	public void push(NotifyInfo notifyInfo, List<SysUser> userList) {
		// 发布事件，不直接在此处进行处理，交给监听者采用实际的 websocket 推送（或者其他方式）
		publisher.publishEvent(new StationNotifyPushEvent(notifyInfo, userList));
	}

}
