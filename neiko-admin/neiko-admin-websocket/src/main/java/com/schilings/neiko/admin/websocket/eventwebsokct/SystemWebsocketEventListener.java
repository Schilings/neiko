package com.schilings.neiko.admin.websocket.eventwebsokct;

import com.schilings.neiko.admin.websocket.jsonmessage.DictChangeMessage;
import com.schilings.neiko.common.util.json.JsonUtils;
import com.schilings.neiko.common.websocket.distribute.MessageDistributor;
import com.schilings.neiko.common.websocket.distribute.dto.MessageDTO;
import com.schilings.neiko.system.event.DictChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
public class SystemWebsocketEventListener {

	private final MessageDistributor messageDistributor;

	/**
	 * 字典修改事件监听
	 * @param event the `DictChangeEvent`
	 */
	@Async
	@EventListener(DictChangeEvent.class)
	public void onDictChangeEvent(DictChangeEvent event) {
		// 构建字典修改的消息体
		DictChangeMessage dictChangeMessage = new DictChangeMessage();
		dictChangeMessage.setDictCode(event.getDictCode());
		String msg = JsonUtils.toJson(dictChangeMessage);

		// 广播修改信息
		MessageDTO messageDO = new MessageDTO().setMessageText(msg).setNeedBroadcast(true);
		messageDistributor.distribute(messageDO);
	}

}
