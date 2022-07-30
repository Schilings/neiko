package com.schilings.neiko.common.core.desensitize.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.schilings.neiko.common.core.desensitize.enums.DesensitizationType;
import com.schilings.neiko.common.core.desensitize.handler.simple.SimpleDesensitizationHandler;
import com.schilings.neiko.common.core.desensitize.handler.simple.SixAsteriskDesensitizationHandler;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JsonDesensitize(type = DesensitizationType.SIMPLE)
public @interface JsonSimpleDesensitize {

	/**
	 * 条件匹配下才进行脱敏
	 */
	@AliasFor(annotation = JsonDesensitize.class)
	String condition() default "";

	/**
	 * 简单脱敏处理器类型，默认{@link SixAsteriskDesensitizationHandler}
	 * @return
	 */
	Class<? extends SimpleDesensitizationHandler> handlerClass() default SixAsteriskDesensitizationHandler.class;

}
