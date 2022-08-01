package com.schilings.neiko.common.excel.annotation;

import com.schilings.neiko.common.excel.handler.request.DefaultListAnalysisEventListener;
import com.schilings.neiko.common.excel.handler.request.ListAnalysisEventListener;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestExcel {

	/**
	 * Alias for {@link #name}.
	 */
	@AliasFor("name")
	String value() default "";

	/**
	 * The name of the part in the {@code "multipart/form-data"} request to bind to.
	 * @since 4.2
	 */
	@AliasFor("value")
	String name() default "";

	/**
	 * 读取的监听器类
	 * @return readListener
	 */
	Class<? extends ListAnalysisEventListener<?>> readListener() default DefaultListAnalysisEventListener.class;

	/**
	 * 是否跳过空行
	 * @return 默认跳过
	 */
	boolean ignoreEmptyRow() default false;

	/**
	 * Whether the part is required.
	 * <p>
	 * Defaults to {@code true}, leading to an exception being thrown if the part is
	 * missing in the request. Switch this to {@code false} if you prefer a {@code null}
	 * value if the part is not present in the request.
	 */
	boolean required() default true;

}
