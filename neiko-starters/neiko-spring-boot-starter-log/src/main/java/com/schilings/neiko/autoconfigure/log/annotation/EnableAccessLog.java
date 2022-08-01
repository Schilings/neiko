package com.schilings.neiko.autoconfigure.log.annotation;

import com.schilings.neiko.autoconfigure.log.AccessLogAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AccessLogAutoConfiguration.class)
public @interface EnableAccessLog {

}
