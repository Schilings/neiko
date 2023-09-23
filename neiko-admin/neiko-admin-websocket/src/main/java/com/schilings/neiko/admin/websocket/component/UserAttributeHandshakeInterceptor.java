package com.schilings.neiko.admin.websocket.component;

import com.schilings.neiko.admin.websocket.constant.AdminWebSocketConstants;
import com.schilings.neiko.authorization.common.userdetails.User;
import com.schilings.neiko.authorization.common.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

import static com.schilings.neiko.admin.websocket.constant.AdminWebSocketConstants.*;

/**
 *
 * <p>
 * WebSocket 握手拦截器 在握手时记录下当前 session 对应的用户Id和token信息
 * </p>
 *
 * @author Schilings
 */
@RequiredArgsConstructor
public class UserAttributeHandshakeInterceptor implements HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) {
		String accessToken = null;
		// 获得 accessToken
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
			accessToken = serverRequest.getServletRequest().getParameter(AdminWebSocketConstants.TOKEN_ATTR_NAME);
		}
		// 由于 WebSocket 握手是由 http 升级的，携带 token 已经被 Security 拦截验证了，所以可以直接获取到用户
		User user = SecurityUtils.getUser();
		attributes.put(AdminWebSocketConstants.TOKEN_ATTR_NAME, accessToken);
		attributes.put(AdminWebSocketConstants.USER_KEY_ATTR_NAME, user.getUserId());
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		// doNothing
	}

}
