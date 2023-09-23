package com.schilings.neiko.common.cache.components;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

/**
 * 缓存仓库，如Redis存储、文件存储等等等等
 */
public interface CacheRepository {

	String getName();

	Object get(String key, Type type) throws IOException;

	Object syncGet(String key, Type type) throws IOException;

	void put(String key, Object value) throws IOException;

	void syncPut(String key, Object value) throws IOException;

	void put(String key, Object value, long ttl, TimeUnit unit) throws IOException;

	void syncPut(String key, Object value, long ttl, TimeUnit unit) throws IOException;

	void evict(String key);

}
