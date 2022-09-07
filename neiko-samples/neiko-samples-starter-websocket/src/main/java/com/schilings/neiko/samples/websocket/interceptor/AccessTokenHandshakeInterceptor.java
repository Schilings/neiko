package com.schilings.neiko.samples.websocket.interceptor;

import com.schilings.neiko.samples.websocket.generator.CustomSessionKeyGenerator;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * 自定义websocket握手连接的拦截器，可以将我们要检验的token等属性放入
 *
 * 注册为bean，会自动注册给WebSocketConfigurer
 *
 * @see com.schilings.neiko.autoconfigure.websocket.WebSocketAutoConfiguration
 */

@Component // extends HttpSessionHandshakeInterceptor
public class AccessTokenHandshakeInterceptor implements HandshakeInterceptor {

	/**
	 * 在处理握手之前调用。 是继续握手 ( true ) 还是中止 ( false )
	 * @param serverHttpRequest 当前请求
	 * @param serverHttpResponse 当前响应
	 * @param webSocketHandler 目标 WebSocket 处理程序
	 * @param attributes 来自 HTTP 握手的与 WebSocket 会话相关联的属性；复制提供的属性，不使用原始地图。
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
			WebSocketHandler webSocketHandler, Map<String, Object> attributes) throws Exception {
		// 由于 WebSocket 握手是由 http 升级的，携带 token 已经被 Security 拦截验证了，所以可以直接获取到用户
		// User user = SecurityUtils.getUser();

		/**
		 * 因为 WebSocketSession 无法获得 ws 地址上的请求参数，所以只好通过该拦截器， 获得 accessToken 请求参数，设置到
		 * attributes 中。代码如下：
		 */
		// 给握手的成功的WebSocketSession放上特定属性，可以从WebSocketSession.getAttributes()拿到

		if (serverHttpRequest instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) serverHttpRequest;
			String accessToken = serverRequest.getServletRequest().getParameter(CustomSessionKeyGenerator.ACCESS_TOKEN);
			attributes.put(CustomSessionKeyGenerator.ACCESS_TOKEN, accessToken);
		}
		return true;
	}

	/**
	 * Invoked after the handshake is done. The response status and headers indicate the
	 * results of the handshake, i.e. whether it was successful or not.
	 * @param serverHttpRequest the current request
	 * @param serverHttpResponse the current response
	 * @param webSocketHandler the target WebSocket handler
	 * @param e an exception raised during the handshake, or {@code null} if none
	 */
	@Override
	public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
			WebSocketHandler webSocketHandler, Exception e) {
		// doNothing
	}

}
