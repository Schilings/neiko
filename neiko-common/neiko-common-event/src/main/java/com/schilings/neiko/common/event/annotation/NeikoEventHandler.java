package com.schilings.neiko.common.event.annotation;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface NeikoEventHandler {
    Class<?> value();
    
}
