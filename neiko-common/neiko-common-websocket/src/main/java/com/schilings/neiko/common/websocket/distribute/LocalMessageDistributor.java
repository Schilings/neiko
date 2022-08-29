package com.schilings.neiko.common.websocket.distribute;

import com.schilings.neiko.common.websocket.distribute.dto.MessageDTO;
import com.schilings.neiko.common.websocket.session.WebSocketSessionStore;

/**
 * 本地消息分发，直接进行发送
 *
 */
public class LocalMessageDistributor extends AbstractMessageDistributor {

	public LocalMessageDistributor(WebSocketSessionStore webSocketSessionStore) {
		super(webSocketSessionStore);
	}

	/**
	 * 消息分发
	 * @param messageDO 发送的消息
	 */
	@Override
	public void distribute(MessageDTO messageDO) {
		doSend(messageDO);
	}

}
