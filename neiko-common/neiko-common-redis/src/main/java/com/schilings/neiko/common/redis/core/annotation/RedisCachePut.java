package com.schilings.neiko.common.redis.core.annotation;

import com.schilings.neiko.common.cache.annotation.NeikoCachePut;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@NeikoCachePut(cacheRepository = "redis")
public @interface RedisCachePut {

	@AliasFor(annotation = NeikoCachePut.class)
	String key() default "";

	/**
	 * 有效时间(S) ttl = 0 使用全局配置值 ttl < 0 : 不超时 ttl > 0 : 使用此超时间
	 */
	@AliasFor(annotation = NeikoCachePut.class)
	long ttl() default -1;

	/**
	 * 控制时长单位，默认为 SECONDS 分钟
	 * @return
	 */
	@AliasFor(annotation = NeikoCachePut.class)
	TimeUnit unit() default TimeUnit.SECONDS;

	@AliasFor(annotation = NeikoCachePut.class)
	String condition() default "";

	@AliasFor(annotation = NeikoCachePut.class)
	String unless() default "";

	@AliasFor(annotation = NeikoCachePut.class)
	boolean sync() default true;

}
