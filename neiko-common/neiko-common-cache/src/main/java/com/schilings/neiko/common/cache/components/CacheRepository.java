package com.schilings.neiko.common.cache.components;


import java.util.concurrent.TimeUnit;

/**
 * 缓存仓库，如Redis存储、文件存储等等等等
 */
public interface CacheRepository {

    String getName();
    
    Object get(String key);
    
    Object syncGet(String key);


    void put(String key, Object value);

    void syncPut(String key, Object value);

    void put(String key, Object value, long ttl, TimeUnit unit);

    void syncPut(String key, Object value, long ttl, TimeUnit unit);

    void evict(String key);
    
    
    
}
