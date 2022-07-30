package com.schilings.neiko.common.cache.operation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public abstract class CacheOperation {

	private final String name;

	/**
	 * 缓存仓库
	 */
	private final String cacheRepository;

	/**
	 * 缓存key
	 */
	private final String key;

	/**
	 * 缓存key生成器
	 */
	private final String keyGenerator;

	/**
	 * 缓存条件
	 */
	private final String condition;

	/**
	 * 有效时间
	 */
	private final long ttl;

	/**
	 * 时间单位
	 */
	private final TimeUnit unit;

	/**
	 * 是否同步加锁
	 */
	private final boolean isSync;

	protected CacheOperation(Builder b) {
		this.name = b.name;
		this.cacheRepository = b.cacheRepository;
		this.key = b.key;
		this.keyGenerator = b.keyGenerator;
		this.condition = b.condition;
		this.ttl = b.ttl;
		this.unit = b.unit;
		this.isSync = b.isSync;
	}

	@Setter
	@Getter
	public abstract static class Builder {

		private String name = "";

		private String cacheRepository = "";

		private String key = "";

		private String keyGenerator = "";

		private String condition = "";

		private long ttl;

		private TimeUnit unit;

		private boolean isSync;

		public void setName(String name) {
			Assert.hasText(name, "Name must not be empty");
			this.name = name;
		}

		public void setCacheRepository(String cacheRepository) {
			this.cacheRepository = cacheRepository;
		}

		public String getCacheRepository() {
			return this.cacheRepository;
		}

		public void setKey(String key) {
			Assert.notNull(key, "Key must not be null");
			this.key = key;
		}

		public String getKey() {
			return this.key;
		}

		public String getKeyGenerator() {
			return this.keyGenerator;
		}

		public void setKeyGenerator(String keyGenerator) {
			this.keyGenerator = keyGenerator;
		}

		public void setCondition(String condition) {
			Assert.notNull(condition, "Condition must not be null");
			this.condition = condition;
		}

		public abstract CacheOperation build();

	}

}
