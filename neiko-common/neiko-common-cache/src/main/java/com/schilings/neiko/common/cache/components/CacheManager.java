package com.schilings.neiko.common.cache.components;



import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * <p>缓存管理器，可以获取所有缓存的信息</p>
 * </pre>
 * @author Schilings
*/
public interface CacheManager {

    /**
     * 获取所有缓存的键
     * @return
     */
    Collection<String> getCacheKeys();
    
    void put(String repository,String key, Object value);

    void put(String repository,String key, Object value, long ttl, TimeUnit unit);
    
    void syncPut(String repository,String key, Object value);

    void syncPut(String repository,String key, Object value, long ttl, TimeUnit unit);

    boolean putIfAbsent(String repository,String key, Object value);

    boolean putIfAbsent(String repository,String key, Object value, long ttl, TimeUnit unit);

    Object get(String repository,String key);
    
    Object syncGet(String repository,String key);
    
    void evict(String repository,String key);

    boolean evictIfPresent(String repository,String key);
    
    void clear(String repository,String key);
    
    

}
