package com.schilings.neiko.autoconfigure.websocket.core;

import com.schilings.neiko.autoconfigure.websocket.properties.WebSocketProperties;
import com.schilings.neiko.common.websocket.core.MapSessionWebSocketHandlerDecorator;
import com.schilings.neiko.common.websocket.core.NeikoWebSocketHandler;
import com.schilings.neiko.common.websocket.handler.PlanTextMessageHandler;
import com.schilings.neiko.common.websocket.session.DefaultWebSocketSessionStore;
import com.schilings.neiko.common.websocket.session.SessionKeyGenerator;
import com.schilings.neiko.common.websocket.session.WebSocketSessionStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;


@RequiredArgsConstructor
public class WebSocketHandlerConfig {

	private final WebSocketProperties webSocketProperties;

	/**
	 * WebSocket session 存储器
	 * @return DefaultWebSocketSessionStore
	 */
	@Bean
	@ConditionalOnMissingBean
	public WebSocketSessionStore webSocketSessionStore(
			@Autowired(required = false) SessionKeyGenerator sessionKeyGenerator) {
		return new DefaultWebSocketSessionStore(sessionKeyGenerator);
	}

	@Bean
	@ConditionalOnMissingBean(WebSocketHandler.class)
	public WebSocketHandler webSocketHandler(WebSocketSessionStore webSocketSessionStore,
											 @Autowired(required = false) PlanTextMessageHandler planTextMessageHandler) {
		NeikoWebSocketHandler webSocketHandler = new NeikoWebSocketHandler(planTextMessageHandler);
		if (webSocketProperties.isMapSession()) {
			return new MapSessionWebSocketHandlerDecorator(webSocketHandler, webSocketSessionStore,
					webSocketProperties.getConcurrent());
		}
		return webSocketHandler;
	}

}