package com.schilings.neiko.common.websocket.message;

public class PingJsonWebSocketMessage extends JsonWebSocketMessage {

	public PingJsonWebSocketMessage() {
		super(WebSocketMessageTypeEnum.PING.getValue());
	}

}
