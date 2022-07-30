package com.schilings.neiko.common.cache.components;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * <p>CacheRepository的简单实现</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
public class LocalCacheRepository extends AbstractCacheRepository {

	public static final String LOACL = "local";

	private final ConcurrentMap<String, Object> cacheMap = new ConcurrentHashMap<>(16);

	@Override
	public String getName() {
		return LOACL;
	}

	@Override
	public Object get(String key) {
		return cacheMap.getOrDefault(key, null);
	}

	@Override
	public Object syncGet(String key) {
		return cacheMap.getOrDefault(key, null);
	}

	@Override
	public void put(String key, Object value) {
		cacheMap.put(key, value);
	}

	@Override
	public void syncPut(String key, Object value) {
		cacheMap.put(key, value);
	}

	@Override
	public void put(String key, Object value, long ttl, TimeUnit unit) {
		cacheMap.put(key, value);
	}

	@Override
	public void syncPut(String key, Object value, long ttl, TimeUnit unit) {
		cacheMap.put(key, value);
	}

	@Override
	public void evict(String key) {
		cacheMap.remove(key);
	}

}
