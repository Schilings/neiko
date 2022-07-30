package com.schilings.neiko.common.core.desensitize.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import com.schilings.neiko.common.core.desensitize.enums.DesensitizationType;
import com.schilings.neiko.common.core.desensitize.enums.SlideDesensitizationTypeEnum;
import com.schilings.neiko.common.core.desensitize.handler.DesensitizationHandler;
import com.schilings.neiko.common.core.desensitize.handler.slide.DefaultSlideDesensitizationHandler;
import com.schilings.neiko.common.core.desensitize.handler.slide.SlideDesensitizationHandler;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JsonDesensitize(type = DesensitizationType.SLIDE)
public @interface JsonSlideDesensitize {

	/**
	 * 条件匹配下才进行脱敏
	 */
	@AliasFor(annotation = JsonDesensitize.class)
	String condition() default "";

	/**
	 * 脱敏类型，只有当值为 CUSTOM 时，以下三个参数才有效
	 * @see SlideDesensitizationTypeEnum#CUSTOM
	 * @return type
	 */
	SlideDesensitizationTypeEnum type();

	/**
	 * 左边的明文数，只有当type值为 CUSTOM 时，才生效
	 */
	int leftPlainTextLen() default 0;

	/**
	 * 右边的明文数，只有当type值为 CUSTOM 时，才生效
	 */
	int rightPlainTextLen() default 0;

	/**
	 * 剩余部分字符逐个替换的字符串，只有当type值为 CUSTOM 时，才生效
	 */
	String maskString() default "*";

	/**
	 * 处理器类型
	 */
	Class<? extends SlideDesensitizationHandler> handlerClass() default DefaultSlideDesensitizationHandler.class;

}
