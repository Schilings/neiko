package com.schilings.neiko.autoconfigure.redis;

import com.schilings.neiko.common.redis.config.RedisCachePropertiesHolder;
import com.schilings.neiko.common.redis.message.factory.DefaultMessageEventListenerFactory;
import com.schilings.neiko.common.redis.message.factory.MessageEventListenerFactory;
import com.schilings.neiko.common.redis.message.MessageEventListenerMethodProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>
 * <p>{@link RedisAutoConfiguration}</p>
 * </pre>
 *
 * @author Schilings
 */
@AutoConfiguration(after = RedisAutoConfiguration.class)
@RequiredArgsConstructor
@Import(MessageEventListenerMethodProcessor.class)
public class MessageEventListenerAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(RedisMessageListenerContainer.class)
	public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		// 设置线程池，控制通过调节线程池来控制队列监听的速率
		if (RedisCachePropertiesHolder.threadCount() > 0) {
			container.setTaskExecutor(Executors.newFixedThreadPool(RedisCachePropertiesHolder.threadCount()));
		}
		return container;
	}

	@Bean
	@ConditionalOnMissingBean
	public MessageEventListenerFactory messageEventListenerFactory() {
		return new DefaultMessageEventListenerFactory();
	}

}
