package com.schilings.neiko.common.cache.components;


import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * <p>CacheErrorHandler的简答实现</p>
 * </pre>
 * @author Schilings
*/
@Slf4j
public class SimpleCacheErrorHandler implements CacheErrorHandler{

    @Override
    public void handleCacheGetError(Exception exception, String key) {
        log.warn("handleCacheGetError key:{},error:{}", key, exception);
    }

    @Override
    public void handleCachePutError(Exception exception, String key, Object value) {
        log.warn("handleCachePutError key:{},value:{},error:{}", key, value,exception);
        throw new RuntimeException(exception);
    }

    @Override
    public void handleCacheEvictError(Exception exception, String key) {
        log.warn("handleCacheEvictError key:{},error:{}", key, exception);
        throw new RuntimeException(exception);
    }

    @Override
    public void handleCacheClearError(Exception exception, String  repository) {
        log.warn("handleCacheClearError repository:{},error:{}", repository, exception);
        throw new RuntimeException(exception);
    }

    @Override
    public void handleCacheSyncGetError(Exception exception, String key) {
        log.warn("handleCacheGetError key:{},error:{}", key, exception);
        throw new RuntimeException(exception);
    }

    @Override
    public void handleCacheSyncPutError(Exception exception, String key, Object value) {
        log.warn("handleCachePutError key:{},value:{},error:{}", key, value,exception);
        throw new RuntimeException(exception);
    }
}
