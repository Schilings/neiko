package com.schilings.neiko.common.redis.core.annotation;

import com.schilings.neiko.common.cache.annotation.NeikoCacheEvict;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@NeikoCacheEvict(cacheRepository = "redis")
public @interface RedisCacheEvict {

    @AliasFor(annotation = NeikoCacheEvict.class)
    String key() default "";
    
    @AliasFor(annotation = NeikoCacheEvict.class)
    String condition() default "";
    
    @AliasFor(annotation = NeikoCacheEvict.class)
    boolean sync() default true;
    
    @AliasFor(annotation = NeikoCacheEvict.class)
    boolean allEntries() default false;

    /**
     * 暂时不起作用
     * @return
     */
    @AliasFor(annotation = NeikoCacheEvict.class)
    boolean beforeInvocation() default false;
}
