package com.schilings.neiko.admin.websocket.component;

import cn.dev33.satoken.oauth2.logic.SaOAuth2Util;
import com.schilings.neiko.common.security.constant.SecurityConstants;
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
 * <p>WebSocket 握手拦截器 在握手时记录下当前 session 对应的用户Id和token信息</p>
 * 
 * @author Schilings
*/
@RequiredArgsConstructor
public class UserAttributeHandshakeInterceptor implements HandshakeInterceptor {


	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
		String accessToken = null;
		// 获得 accessToken
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
			accessToken = serverRequest.getServletRequest().getParameter(TOKEN_ATTR_NAME);
		}
		//验证Token
		Object userId = SaOAuth2Util.getLoginIdByAccessToken(accessToken);
		attributes.put(TOKEN_ATTR_NAME, accessToken);
		attributes.put(USER_KEY_ATTR_NAME, userId);
		return true;
	}


	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		// doNothing
	}

}
