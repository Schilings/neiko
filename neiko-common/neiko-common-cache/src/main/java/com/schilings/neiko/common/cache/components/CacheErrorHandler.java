package com.schilings.neiko.common.cache.components;


import org.springframework.lang.Nullable;

/**
 * <pre>
 * <p>缓存时发生的错误处理器</p>
 * </pre>
 * @author Schilings
*/
public interface CacheErrorHandler {

    void handleCacheGetError(RuntimeException exception, String key);

    void handleCachePutError(RuntimeException exception, String key, @Nullable Object value);

    void handleCacheEvictError(RuntimeException exception,String key);

    void handleCacheClearError(RuntimeException exception,CacheManager cacheManager);
}
