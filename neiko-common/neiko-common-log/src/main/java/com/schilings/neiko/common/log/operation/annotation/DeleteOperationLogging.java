package com.schilings.neiko.common.log.operation.annotation;


import com.schilings.neiko.common.log.operation.enums.OperationTypeEnum;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@OperationLogging(type = OperationTypeEnum.DELETE)
public @interface DeleteOperationLogging {

	/**
	 * 日志信息
	 * @return 日志描述信息
	 */
	@AliasFor(annotation = OperationLogging.class)
	String msg();

	/**
	 * 是否保存方法入参
	 * @return boolean
	 */
	@AliasFor(annotation = OperationLogging.class)
	boolean recordParams() default true;

	/**
	 * 是否保存方法返回值
	 * @return boolean
	 */
	@AliasFor(annotation = OperationLogging.class)
	boolean recordResult() default true;

}
