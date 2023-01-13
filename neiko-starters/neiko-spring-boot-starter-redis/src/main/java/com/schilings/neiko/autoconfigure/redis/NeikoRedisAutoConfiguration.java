package com.schilings.neiko.autoconfigure.redis;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.schilings.neiko.common.cache.EnableNeikoCaching;
import com.schilings.neiko.common.cache.components.CacheRepository;
import com.schilings.neiko.common.redis.RedisHelper;
import com.schilings.neiko.common.redis.config.RedisCacheProperties;
import com.schilings.neiko.common.redis.config.RedisCachePropertiesHolder;
import com.schilings.neiko.common.redis.core.lock.RedisCacheLock;
import com.schilings.neiko.common.redis.core.prefix.IRedisPrefixConverter;
import com.schilings.neiko.common.redis.core.prefix.impl.DefaultRedisPrefixConverter;
import com.schilings.neiko.common.redis.core.repository.RedisCacheRepository;
import com.schilings.neiko.common.redis.core.serializer.CacheSerializer;
import com.schilings.neiko.common.redis.core.serializer.JacksonSerializer;
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
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.schilings.neiko.common.redis.RedisHelper.*;

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

	@Bean("ObjectRedisTemplate")
	@ConditionalOnBean(IRedisPrefixConverter.class)
	@ConditionalOnMissingBean(name = "ObjectRedisTemplate") // before =
															// RedisAutoConfiguration.class
	public RedisTemplate<String, Object> redisTemplate(IRedisPrefixConverter redisPrefixConverter) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new PrefixStringRedisSerializer(redisPrefixConverter));
		template.setHashKeySerializer(new StringRedisSerializer());
		GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
		try {
			Field field = GenericJackson2JsonRedisSerializer.class.getDeclaredField("mapper");
			field.setAccessible(true);
			ObjectMapper objectMapper = (ObjectMapper) field.get(valueSerializer);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			JavaTimeModule timeModule = new JavaTimeModule();
			timeModule.addSerializer(new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
			timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER));
			timeModule.addSerializer(new LocalDateSerializer(DATE_FORMATTER));
			timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER));
			timeModule.addSerializer(new LocalTimeSerializer(TIME_FORMATTER));
			timeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(TIME_FORMATTER));
			objectMapper.registerModule(timeModule);
		}
		catch (Exception var7) {
			System.err.println(var7.getMessage());
		}
		template.setValueSerializer(valueSerializer);
		template.setHashValueSerializer(valueSerializer);
		return template;
	}

	@Bean
	@ConditionalOnMissingBean(RedisHelper.class)
	public RedisHelper redisHelper(StringRedisTemplate template, RedisTemplate<String, Object> redisTemplate) {
		RedisHelper.setTemplate(template);
		RedisHelper.setObjectRedisTemplate(redisTemplate);
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
