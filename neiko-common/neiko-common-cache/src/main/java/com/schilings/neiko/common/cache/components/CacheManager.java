package com.schilings.neiko.common.cache.components;

import java.io.IOException;
import java.lang.reflect.Type;
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
	// Collection<String> getCacheKeys() throws IOException;

	void put(String repository, String key, Object value) throws IOException;

	void put(String repository, String key, Object value, long ttl, TimeUnit unit) throws IOException;

	void syncPut(String repository, String key, Object value) throws IOException;

	void syncPut(String repository, String key, Object value, long ttl, TimeUnit unit) throws IOException;

	Object get(String repository, String key, Type javaTyp) throws IOException;

	Object syncGet(String repository, String key, Type javaTyp) throws IOException;

	void evict(String repository, String key);

}
