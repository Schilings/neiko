package com.schilings.neiko.common.excel.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * <pre>
 * <p></p>
 * </pre>
 * @author Schilings
 */
@Deprecated
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelProperty {

	@AliasFor("head")
	String[] value() default {};

	/**
	 * 标题
	 * @return
	 */
	@AliasFor("value")
	String[] head() default {};

	/**
	 * 是否忽略标题
	 * @return
	 */
	boolean ignoreHead() default false;

}
