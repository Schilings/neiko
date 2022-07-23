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
    public void handleCacheGetError(RuntimeException exception, String key) {
        log.trace("handleCacheGetError key:{},error:{}", key, exception);
        throw exception;
    }

    @Override
    public void handleCachePutError(RuntimeException exception, String key, Object value) {
        log.trace("handleCachePutError key:{},value:{},error:{}", key, value,exception);
        throw exception;
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, String key) {
        log.trace("handleCacheEvictError key:{},error:{}", key, exception);
        throw exception;
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, CacheManager cacheManager) {
        log.trace("handleCacheClearError cacheManager:{},error:{}", cacheManager, exception);
        throw exception;
    }
}
