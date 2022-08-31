package com.schilings.neiko.admin.websocket.constant;

import com.schilings.neiko.common.security.constant.SecurityConstants;


public final class AdminWebSocketConstants {

	private AdminWebSocketConstants() {
	}

	/**
	 * 存储在 WebSocketSession Attribute 中的 token 属性名
	 */
	public static final String TOKEN_ATTR_NAME = SecurityConstants.Param.access_token;

	/**
	 * 存储在 WebSocketSession Attribute 中的 用户唯一标识 属性名
	 */
	public static final String USER_KEY_ATTR_NAME = "userId";

}
