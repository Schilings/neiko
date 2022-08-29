package com.schilings.neiko.common.websocket.handler;

import com.schilings.neiko.common.websocket.core.WebSocketMessageSender;
import com.schilings.neiko.common.websocket.message.JsonWebSocketMessage;
import com.schilings.neiko.common.websocket.message.PingJsonWebSocketMessage;
import com.schilings.neiko.common.websocket.message.PongJsonWebSocketMessage;
import com.schilings.neiko.common.websocket.message.WebSocketMessageTypeEnum;
import org.springframework.web.socket.WebSocketSession;

/**
 * 心跳处理，接收到客户端的ping时，立刻回复一个pong
 */
public class PingJsonMessageHandler implements JsonMessageHandler<PingJsonWebSocketMessage> {

	@Override
	public void handle(WebSocketSession session, PingJsonWebSocketMessage message) {
		JsonWebSocketMessage pongJsonWebSocketMessage = new PongJsonWebSocketMessage();
		WebSocketMessageSender.send(session, pongJsonWebSocketMessage);
	}

	@Override
	public String type() {
		return WebSocketMessageTypeEnum.PING.getValue();
	}

	@Override
	public Class<PingJsonWebSocketMessage> getMessageClass() {
		return PingJsonWebSocketMessage.class;
	}

}
