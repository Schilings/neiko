package com.schilings.neiko.common.cache.components;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * <p>缓存管理器，可以获取所有缓存的信息</p>
 * </pre>
 *
 * @author Schilings
 */
public interface CacheManager {

	/**
	 * 获取所有缓存的键
	 * @return
	 */
	Collection<String> getCacheKeys();

	void put(String repository, String key, Object value) throws IOException;

	void put(String repository, String key, Object value, long ttl, TimeUnit unit) throws IOException;

	void syncPut(String repository, String key, Object value) throws IOException;

	void syncPut(String repository, String key, Object value, long ttl, TimeUnit unit) throws IOException;

	boolean putIfAbsent(String repository, String key, Object value) throws IOException;

	boolean putIfAbsent(String repository, String key, Object value, long ttl, TimeUnit unit) throws IOException;

	Object get(String repository, String key) throws IOException;

	Object syncGet(String repository, String key) throws IOException;

	void evict(String repository, String key);

	boolean evictIfPresent(String repository, String key) throws IOException;

	void clear(String repository);

}
