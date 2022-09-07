package com.schilings.neiko.samples.websocket.handler;

import com.schilings.neiko.common.websocket.core.WebSocketMessageSender;
import com.schilings.neiko.common.websocket.handler.PlanTextMessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
@RequiredArgsConstructor
public class CustomPlanTextHandle implements PlanTextMessageHandler {

	@Override
	public void handle(WebSocketSession webSocketSession, String s) {
		// 原样返回
		WebSocketMessageSender.send(webSocketSession, s);

	}

}
