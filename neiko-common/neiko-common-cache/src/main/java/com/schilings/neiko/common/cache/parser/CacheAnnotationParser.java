package com.schilings.neiko.common.cache.parser;



import com.schilings.neiko.common.cache.operation.CacheOperation;


import java.lang.reflect.Method;
import java.util.Collection;

public interface CacheAnnotationParser {

    default boolean isCandidateClass(Class<?> targetClass) {
        return true;
    }

    Collection<CacheOperation> parseCacheAnnotations(Class<?> type);

    Collection<CacheOperation> parseCacheAnnotations(Method method);

    Collection<CacheOperation> getCacheOperations(Method method, Class<?> targetClass);
}
