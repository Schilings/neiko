package com.schilings.neiko.admin.websocket;

import com.schilings.neiko.admin.websocket.eventwebsokct.SystemWebsocketEventListener;
import com.schilings.neiko.common.websocket.distribute.MessageDistributor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@ConditionalOnClass(SystemWebsocketEventListener.class)
@Configuration(proxyBeanMethods = false)
public class SystemWebsocketEventListenerConfiguration {

	private final MessageDistributor messageDistributor;

	@Bean
	public SystemWebsocketEventListener systemWebsocketEventListener() {
		return new SystemWebsocketEventListener(messageDistributor);
	}

}
