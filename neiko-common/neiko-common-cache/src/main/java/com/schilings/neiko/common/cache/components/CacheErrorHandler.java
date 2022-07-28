package com.schilings.neiko.common.cache.components;


import org.springframework.lang.Nullable;

/**
 * <pre>
 * <p>缓存时发生的错误处理器</p>
 * </pre>
 * @author Schilings
*/
public interface CacheErrorHandler {

    void handleCacheGetError(Exception exception, String key);

    void handleCachePutError(Exception exception, String key, @Nullable Object value);

    void handleCacheEvictError(Exception exception,String key);

    void handleCacheClearError(Exception exception,String repository);

    void handleCacheSyncGetError(Exception exception, String key);

    void handleCacheSyncPutError(Exception exception, String key, @Nullable Object value);
    
}
