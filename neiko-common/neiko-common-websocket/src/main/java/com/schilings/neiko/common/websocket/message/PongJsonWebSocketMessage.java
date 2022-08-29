package com.schilings.neiko.common.websocket.message;

public class PongJsonWebSocketMessage extends JsonWebSocketMessage {

	public PongJsonWebSocketMessage() {
		super(WebSocketMessageTypeEnum.PONG.getValue());
	}

}
