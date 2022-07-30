package com.schilings.neiko.autoconfigure.web.api.annotation;

import java.lang.annotation.*;

/**
 * <pre>{@code
 *
 * }
 * <p>接口版本</p>
 * </pre>
 * @author Schilings
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersion {

	String value() default "0.0.0";

}
