package com.schilings.neiko.common.websocket.holder;

import com.schilings.neiko.common.websocket.handler.JsonMessageHandler;
import com.schilings.neiko.common.websocket.message.JsonWebSocketMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JsonMessageHandlerHolder {

	private JsonMessageHandlerHolder() {
	}

	private static final Map<String, JsonMessageHandler<JsonWebSocketMessage>> MESSAGE_HANDLER_MAP = new ConcurrentHashMap<>();

	public static JsonMessageHandler<JsonWebSocketMessage> getHandler(String type) {
		return MESSAGE_HANDLER_MAP.get(type);
	}

	public static void addHandler(JsonMessageHandler<JsonWebSocketMessage> jsonMessageHandler) {
		MESSAGE_HANDLER_MAP.put(jsonMessageHandler.type(), jsonMessageHandler);
	}

}
