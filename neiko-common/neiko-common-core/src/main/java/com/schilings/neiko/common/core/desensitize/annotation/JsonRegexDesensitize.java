package com.schilings.neiko.common.core.desensitize.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.schilings.neiko.common.core.desensitize.enums.DesensitizationType;
import com.schilings.neiko.common.core.desensitize.enums.RegexDesensitizationTypeEnum;
import com.schilings.neiko.common.core.desensitize.handler.DesensitizationHandler;
import com.schilings.neiko.common.core.desensitize.handler.regex.DefaultRegexDesensitizationHandler;
import com.schilings.neiko.common.core.desensitize.handler.regex.RegexDesensitizationHandler;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * <pre>
 * <p>{@link RegexDesensitizationHandler}</p>
 * </pre>
 * @author Schilings
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JsonDesensitize(type = DesensitizationType.REGEX)
public @interface JsonRegexDesensitize {

	/**
	 * 条件匹配下才进行脱敏
	 */
	@AliasFor(annotation = JsonDesensitize.class)
	String condition() default "";

	/**
	 * 脱敏类型，用于指定正则处理方式。 只有当值为 CUSTOM 时，以下两个个参数才有效
	 * @see RegexDesensitizationTypeEnum#CUSTOM
	 * @return type
	 */
	RegexDesensitizationTypeEnum type();

	/**
	 * 匹配的正则表达式
	 */
	String regex() default "";

	/**
	 * 替换规则，只有当type值为 CUSTOM 时，才生效
	 */
	String replacement() default "******";

	/**
	 * 处理器类型
	 */
	Class<? extends RegexDesensitizationHandler> handlerClass() default DefaultRegexDesensitizationHandler.class;

}
