package com.schilings.neiko.admin.websocket.component;

import com.schilings.neiko.admin.websocket.constant.AdminWebSocketConstants;
import com.schilings.neiko.common.websocket.session.SessionKeyGenerator;
import org.springframework.web.socket.WebSocketSession;

/**
 * <p>
 * 用户 WebSocketSession 唯一标识生成器
 * </p>
 *
 * 此类主要使用当前 session 对应用户的唯一标识做为 session 的唯一标识 方便系统快速通过用户获取对应 session
 */
public class UserSessionKeyGenerator implements SessionKeyGenerator {

	/**
	 * 获取当前session的唯一标识，用户的唯一标识已经通过
	 * @see UserAttributeHandshakeInterceptor 存储在当前 session 的属性中
	 * @param webSocketSession 当前session
	 * @return session唯一标识
	 */
	@Override
	public Object sessionKey(WebSocketSession webSocketSession) {
		return Long.valueOf((String) webSocketSession.getAttributes().get(AdminWebSocketConstants.USER_KEY_ATTR_NAME));
	}

}
