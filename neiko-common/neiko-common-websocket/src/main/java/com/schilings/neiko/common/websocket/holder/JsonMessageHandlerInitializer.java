package com.schilings.neiko.common.websocket.holder;

import com.schilings.neiko.common.websocket.handler.JsonMessageHandler;
import com.schilings.neiko.common.websocket.message.JsonWebSocketMessage;
import lombok.RequiredArgsConstructor;

import jakarta.annotation.PostConstruct;
import java.util.List;

/**
 * <p>
 * JsonMessageHandler 初始化器
 * <p/>
 * 将所有的 jsonMessageHandler 收集到 JsonMessageHandlerHolder 中
 */
@RequiredArgsConstructor
public class JsonMessageHandlerInitializer {

	private final List<JsonMessageHandler<? extends JsonWebSocketMessage>> jsonMessageHandlerList;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void initJsonMessageHandlerHolder() {
		for (JsonMessageHandler<? extends JsonWebSocketMessage> jsonMessageHandler : jsonMessageHandlerList) {
			JsonMessageHandlerHolder.addHandler((JsonMessageHandler<JsonWebSocketMessage>) jsonMessageHandler);
		}
	}

}
