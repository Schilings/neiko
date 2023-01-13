package com.schilings.neiko.common.cache.components;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.Type;
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
	public void put(String repository, String key, Object value) throws IOException {
		getCacheRepository(repository).put(key, value);
	}

	public void put(String repository, String key, Object value, long ttl, TimeUnit unit) throws IOException {
		getCacheRepository(repository).put(key, value, ttl, unit);
	}

	@Override
	public void syncPut(String repository, String key, Object value) throws IOException {
		getCacheRepository(repository).syncPut(key, value);
	}

	@Override
	public void syncPut(String repository, String key, Object value, long ttl, TimeUnit unit) throws IOException {
		getCacheRepository(repository).syncPut(key, value, ttl, unit);
	}

	@Override
	public Object get(String repository, String key, Type type) throws IOException {
		return getCacheRepository(repository).get(key, type);
	}

	@Override
	public Object syncGet(String repository, String key, Type type) throws IOException {
		return getCacheRepository(repository).syncGet(key, type);
	}

	@Override
	public void evict(String repository, String key) {
		getCacheRepository(repository).evict(key);
	}

}
