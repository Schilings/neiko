package com.schilings.neiko.common.cache;

import com.schilings.neiko.common.cache.selector.NeikoCachingConfigurationSelector;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * <pre>
 * <p>{@link EnableCaching}</p>
 * </pre>
 * @author Schilings
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(NeikoCachingConfigurationSelector.class)
public @interface EnableNeikoCaching {

	boolean proxyTargetClass() default false;

	AdviceMode mode() default AdviceMode.PROXY;

	int order() default Ordered.LOWEST_PRECEDENCE;

}
