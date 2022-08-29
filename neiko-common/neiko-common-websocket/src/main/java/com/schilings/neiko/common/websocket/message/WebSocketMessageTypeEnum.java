package com.schilings.neiko.common.websocket.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WebSocketMessageTypeEnum {

	PING("ping"), PONG("pong");

	private final String value;

}
