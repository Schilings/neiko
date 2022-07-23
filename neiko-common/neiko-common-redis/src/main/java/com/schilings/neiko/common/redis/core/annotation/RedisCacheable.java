package com.schilings.neiko.common.redis.core.annotation;


import com.schilings.neiko.common.cache.annotation.NeikoCacheable;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@NeikoCacheable(cacheRepository = "local")
public @interface RedisCacheable {

    @AliasFor(annotation = NeikoCacheable.class)
    String key() default "";

    /**
     * 有效时间(S)
     * ttl = 0 使用全局配置值
     * ttl < 0 : 不超时
     * ttl > 0 : 使用此超时间
     */
    @AliasFor(annotation = NeikoCacheable.class)
    long ttl() default -1;


    /**
     * 控制时长单位，默认为 SECONDS 分钟
     * @return
     */
    @AliasFor(annotation = NeikoCacheable.class)
    TimeUnit unit() default TimeUnit.SECONDS;
}
