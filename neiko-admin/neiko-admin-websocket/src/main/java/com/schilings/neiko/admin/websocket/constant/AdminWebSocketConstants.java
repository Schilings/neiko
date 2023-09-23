package com.schilings.neiko.admin.websocket.constant;

import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

public final class AdminWebSocketConstants {

	private AdminWebSocketConstants() {
	}

	/**
	 * 存储在 WebSocketSession Attribute 中的 token 属性名
	 */
	public static final String TOKEN_ATTR_NAME = OAuth2ParameterNames.ACCESS_TOKEN;

	/**
	 * 存储在 WebSocketSession Attribute 中的 用户唯一标识 属性名
	 */
	public static final String USER_KEY_ATTR_NAME = "userId";

}
