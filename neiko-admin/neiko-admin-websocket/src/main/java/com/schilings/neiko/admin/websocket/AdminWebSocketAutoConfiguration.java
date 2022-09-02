package com.schilings.neiko.admin.websocket;

import com.schilings.neiko.admin.websocket.component.UserAttributeHandshakeInterceptor;
import com.schilings.neiko.admin.websocket.component.UserSessionKeyGenerator;
import com.schilings.neiko.common.websocket.session.SessionKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Import({ NotifyWebsocketEventListenerConfiguration.class, SystemWebsocketEventListenerConfiguration.class })
@AutoConfiguration
@RequiredArgsConstructor
public class AdminWebSocketAutoConfiguration {

	/**
	 * 握手拦截
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(UserAttributeHandshakeInterceptor.class)
	public HandshakeInterceptor authenticationHandshakeInterceptor() {
		return new UserAttributeHandshakeInterceptor();
	}

	/**
	 * 会话Key生成
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(SessionKeyGenerator.class)
	public SessionKeyGenerator userSessionKeyGenerator() {
		return new UserSessionKeyGenerator();
	}

}
