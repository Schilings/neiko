package com.schilings.neiko.autoconfigure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schilings.neiko.common.cache.EnableNeikoCaching;
import com.schilings.neiko.common.cache.components.CacheRepository;
import com.schilings.neiko.common.cache.configuration.NeikoCachingConfiguration;
import com.schilings.neiko.common.redis.RedisHelper;
import com.schilings.neiko.common.redis.config.RedisCacheProperties;
import com.schilings.neiko.common.redis.config.RedisCachePropertiesHolder;
import com.schilings.neiko.common.redis.core.lock.RedisCacheLock;
import com.schilings.neiko.common.redis.core.prefix.IRedisPrefixConverter;
import com.schilings.neiko.common.redis.core.prefix.impl.DefaultRedisPrefixConverter;
import com.schilings.neiko.common.redis.core.repository.RedisCacheRepository;
import com.schilings.neiko.common.redis.core.serializer.CacheSerializer;
import com.schilings.neiko.common.redis.core.serializer.JacksonSerializer;
import com.schilings.neiko.common.redis.core.serializer.PrefixJdkRedisSerializer;
import com.schilings.neiko.common.redis.core.serializer.PrefixStringRedisSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@AutoConfiguration(before = { RedisAutoConfiguration.class })
@EnableNeikoCaching
@RequiredArgsConstructor
@EnableConfigurationProperties(RedisCacheProperties.class)
public class NeikoRedisAutoConfiguration {

	private final RedisConnectionFactory redisConnectionFactory;

	/**
	 * 初始化配置类
	 * @return GlobalCacheProperties
	 */
	@Bean
	@ConditionalOnMissingBean
	public RedisCachePropertiesHolder cachePropertiesHolder(RedisCacheProperties cacheProperties) {
		RedisCachePropertiesHolder cachePropertiesHolder = new RedisCachePropertiesHolder();
		cachePropertiesHolder.setCacheProperties(cacheProperties);
		return cachePropertiesHolder;
	}

	/**
	 * redis key 前缀处理器
	 * @return IRedisPrefixConverter
	 */
	@Bean
	@DependsOn("cachePropertiesHolder")
	@ConditionalOnProperty(prefix = "neiko.redis", name = "key-prefix", matchIfMissing = true)
	@ConditionalOnMissingBean(IRedisPrefixConverter.class)
	public IRedisPrefixConverter redisPrefixConverter() {
		return new DefaultRedisPrefixConverter(RedisCachePropertiesHolder.keyPrefix());
	}

	/**
	 * 默认使用 Jackson 序列化
	 * @param objectMapper objectMapper
	 * @return JacksonSerializer
	 */
	@Bean
	@ConditionalOnMissingBean
	public CacheSerializer cacheSerializer(ObjectMapper objectMapper) {
		return new JacksonSerializer(objectMapper);
	}

	@Bean
	@ConditionalOnBean(IRedisPrefixConverter.class)
	@ConditionalOnMissingBean // before = RedisAutoConfiguration.class
	public StringRedisTemplate stringRedisTemplate(IRedisPrefixConverter redisPrefixConverter) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new PrefixStringRedisSerializer(redisPrefixConverter));
		return template;
	}

	@Bean
	@ConditionalOnBean(IRedisPrefixConverter.class)
	@ConditionalOnMissingBean(name = "redisTemplate") // before =
														// RedisAutoConfiguration.class
	public RedisTemplate<Object, Object> redisTemplate(IRedisPrefixConverter redisPrefixConverter) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new PrefixJdkRedisSerializer(redisPrefixConverter));
		// 使用Redis提供的Json序列化作为值序列化器
		template.setValueSerializer(RedisSerializer.json());
		return template;
	}

	@Bean
	@ConditionalOnMissingBean(RedisHelper.class)
	public RedisHelper redisHelper(StringRedisTemplate template) {
		RedisHelper.setTemplate(template);
		return new RedisHelper();
	}

	/**
	 * 初始化RedisCacheLock
	 * @param stringRedisTemplate 默认使用字符串类型操作，后续扩展
	 * @return RedisCacheLock
	 */
	@Bean
	@ConditionalOnMissingBean
	public RedisCacheLock cacheLock(StringRedisTemplate stringRedisTemplate) {
		RedisCacheLock cacheLock = new RedisCacheLock();
		cacheLock.setStringRedisTemplate(stringRedisTemplate);
		return cacheLock;
	}

	@Bean // before = NeikoCachingConfiguration.class
	@ConditionalOnBean(value = { CacheSerializer.class, StringRedisTemplate.class })
	public CacheRepository redisCacheRepository(CacheSerializer cacheSerializer,
			StringRedisTemplate stringRedisTemplate) {
		return new RedisCacheRepository(cacheSerializer, stringRedisTemplate);
	}

}
