package com.schilings.neiko.common.websocket.distribute;

import cn.hutool.core.collection.CollectionUtil;

import com.schilings.neiko.common.websocket.distribute.dto.MessageDTO;
import com.schilings.neiko.common.websocket.core.WebSocketMessageSender;
import com.schilings.neiko.common.websocket.session.WebSocketSessionStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;

@Slf4j
public abstract class AbstractMessageDistributor implements MessageDistributor {

	private final WebSocketSessionStore webSocketSessionStore;

	protected AbstractMessageDistributor(WebSocketSessionStore webSocketSessionStore) {
		this.webSocketSessionStore = webSocketSessionStore;
	}

	/**
	 * 对当前服务中的 websocket 连接做消息推送
	 * @param messageDTO 消息实体
	 */
	protected void doSend(MessageDTO messageDTO) {

		// 是否广播发送
		Boolean needBroadcast = messageDTO.getNeedBroadcast();

		// 获取待发送的 sessionKeys
		Collection<Object> sessionKeys;
		if (needBroadcast != null && needBroadcast) {
			sessionKeys = webSocketSessionStore.getSessionKeys();
		}
		else {
			sessionKeys = messageDTO.getSessionKeys();
		}
		if (CollectionUtil.isEmpty(sessionKeys)) {
			log.warn("发送 websocket 消息，确没有找到对应 sessionKeys, messageDo: {}", messageDTO);
			return;
		}

		String messageText = messageDTO.getMessageText();
		Boolean onlyOneClientInSameKey = messageDTO.getOnlyOneClientInSameKey();

		for (Object sessionKey : sessionKeys) {
			Collection<WebSocketSession> sessions = webSocketSessionStore.getSessions(sessionKey);
			if (CollectionUtil.isNotEmpty(sessions)) {
				// 相同 sessionKey 的客户端只推送一次操作
				if (onlyOneClientInSameKey != null && onlyOneClientInSameKey) {
					WebSocketSession wsSession = CollectionUtil.get(sessions, 0);
					WebSocketMessageSender.send(wsSession, messageText);
					continue;
				}
				for (WebSocketSession wsSession : sessions) {
					WebSocketMessageSender.send(wsSession, messageText);
				}
			}
		}
	}

}
