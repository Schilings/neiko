package com.schilings.neiko.common.cache.components;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <pre>
 * <p>CacheManager的骨架实现</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
public abstract class AbstractCacheManager implements CacheManager {

	/**
	 * 缓存仓库
	 */
	@Getter
	@Setter
	private Map<String, CacheRepository> cacheRepositoryMapping;

	@Getter
	@Setter
	private Set<String> cacheKeys = new LinkedHashSet<>();

	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	protected AbstractCacheManager(List<CacheRepository> repositories) {
		cacheRepositoryMapping = new LinkedHashMap<>();
		for (CacheRepository repository : repositories) {
			cacheRepositoryMapping.put(repository.getName(), repository);
		}
	}

	/**
	 * 根据名字取出CacheRepository
	 * @param name
	 * @return
	 */
	protected CacheRepository getCacheRepository(String name) {
		CacheRepository repository = cacheRepositoryMapping.get(name);
		Assert.notNull(repository,
				"The CacheRepository[" + name + "] do not exist.There is no bean for CacheRepository[" + name + "]");
		return repository;
	}

	void acquiceReadLock() {
		lock.readLock().lock();
	}

	void releaseReadLock() {
		lock.readLock().unlock();
	}

	void acquiceWriteLock() {
		lock.writeLock().lock();
	}

	void releaseWriteLock() {
		lock.writeLock().unlock();
	}

	@Override
	public Set<String> getCacheKeys() {
		return this.cacheKeys;
	}

	@Override
	public void put(String repository, String key, Object value) throws IOException {
		getCacheRepository(repository).put(key, value);
		cacheKeys.add(key);
	}

	public void put(String repository, String key, Object value, long ttl, TimeUnit unit) throws IOException {
		getCacheRepository(repository).put(key, value, ttl, unit);
		;
		cacheKeys.add(key);
	}

	@Override
	public void syncPut(String repository, String key, Object value) throws IOException {
		acquiceWriteLock();
		try {
			getCacheRepository(repository).syncPut(key, value);
			cacheKeys.add(key);
		}
		finally {
			releaseWriteLock();
		}
	}

	@Override
	public void syncPut(String repository, String key, Object value, long ttl, TimeUnit unit) throws IOException {
		acquiceWriteLock();
		try {
			getCacheRepository(repository).syncPut(key, value, ttl, unit);
			cacheKeys.add(key);
		}
		finally {
			releaseWriteLock();
		}
	}

	@Override
	public boolean putIfAbsent(String repository, String key, Object value) throws IOException {
		Object oldValue = getCacheRepository(repository).get(key);
		if (oldValue == null) {
			getCacheRepository(repository).syncPut(key, value);
			cacheKeys.add(key);
			return true;
		}
		return false;
	}

	@Override
	public boolean putIfAbsent(String repository, String key, Object value, long ttl, TimeUnit unit)
			throws IOException {
		Object oldValue = getCacheRepository(repository).get(key);
		if (oldValue == null) {
			getCacheRepository(repository).syncPut(key, value, ttl, unit);
			cacheKeys.add(key);
			return true;
		}
		return false;
	}

	public Object get(String repository, String key) throws IOException {
		return getCacheRepository(repository).get(key);
	}

	@Override
	public Object syncGet(String repository, String key) throws IOException {
		acquiceReadLock();
		try {
			return getCacheRepository(repository).syncGet(key);
		}
		finally {
			releaseReadLock();
		}
	}

	@Override
	public void evict(String repository, String key) {
		getCacheRepository(repository).evict(key);
		cacheKeys.remove(key);
	}

	@Override
	public void clear(String repository) {
		for (String cachesKey : cacheKeys) {
			getCacheRepository(repository).evict(cachesKey);
			cacheKeys.remove(cachesKey);
		}
	}

	@Override
	public boolean evictIfPresent(String repository, String key) throws IOException {
		Object oldValue = getCacheRepository(repository).get(key);
		if (oldValue != null) {
			getCacheRepository(repository).evict(key);
			cacheKeys.remove(key);
			return true;
		}
		return false;
	}

}
