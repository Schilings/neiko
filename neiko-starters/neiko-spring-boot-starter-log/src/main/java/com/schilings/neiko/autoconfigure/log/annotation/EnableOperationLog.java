package com.schilings.neiko.autoconfigure.log.annotation;

import com.schilings.neiko.autoconfigure.log.OperationLogAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(OperationLogAutoConfiguration.class)
public @interface EnableOperationLog {

}
