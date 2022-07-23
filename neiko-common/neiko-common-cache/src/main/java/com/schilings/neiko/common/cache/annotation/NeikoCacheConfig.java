package com.schilings.neiko.common.cache.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NeikoCacheConfig {
    
    String cacheRepository() default "";

    String keyGenerator() default "";
    
    String cacheManager() default "";
    
    String cacheResolver() default "";

}
