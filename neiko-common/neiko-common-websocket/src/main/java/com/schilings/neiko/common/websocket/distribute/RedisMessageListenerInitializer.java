package com.schilings.neiko.common.websocket.distribute;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import jakarta.annotation.PostConstruct;

/**
 * 初始化 redis 消息的监听器
 */
@RequiredArgsConstructor
public class RedisMessageListenerInitializer {

	private final RedisMessageListenerContainer redisMessageListenerContainer;

	private final RedisMessageDistributor redisWebsocketMessageListener;

	@PostConstruct
	public void addMessageListener() {
		redisMessageListenerContainer.addMessageListener(redisWebsocketMessageListener,
				new PatternTopic(RedisMessageDistributor.CHANNEL));
	}

}