package com.schilings.neiko.autoconfigure.websocket.distribute;

import com.schilings.neiko.autoconfigure.websocket.properties.MessageDistributorTypeConstants;
import com.schilings.neiko.autoconfigure.websocket.properties.WebSocketProperties;
import com.schilings.neiko.common.websocket.distribute.LocalMessageDistributor;
import com.schilings.neiko.common.websocket.distribute.MessageDistributor;
import com.schilings.neiko.common.websocket.session.WebSocketSessionStore;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 本地的消息分发器配置
 */
@ConditionalOnProperty(prefix = WebSocketProperties.PREFIX, name = "message-distributor",
		havingValue = MessageDistributorTypeConstants.LOCAL, matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class LocalMessageDistributorConfig {

	private final WebSocketSessionStore webSocketSessionStore;

	/**
	 * 本地基于内存的消息分发，不支持集群
	 * @return LocalMessageDistributor
	 */
	@Bean
	@ConditionalOnMissingBean(MessageDistributor.class)
	public LocalMessageDistributor messageDistributor() {
		return new LocalMessageDistributor(webSocketSessionStore);
	}

}
