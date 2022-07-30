package com.schilings.neiko.common.core.desensitize.handler.regex;

import com.schilings.neiko.common.core.desensitize.annotation.JsonRegexDesensitize;
import com.schilings.neiko.common.core.desensitize.enums.RegexDesensitizationTypeEnum;
import com.schilings.neiko.common.core.desensitize.handler.DesensitizationHandler;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;

/**
 * <pre>
 * <p>JsonRegexDesensitize对应的处理器接口</p>
 * </pre>
 *
 * @author Schilings
 */
public interface RegexDesensitizationHandler extends DesensitizationHandler {

	String handle(String origin, String regex, String replacement);

	default String handle(String origin, RegexDesensitizationTypeEnum type) {
		return handle(origin, type.getRegex(), type.getReplacement());
	}

	@Override
	default String handle(Annotation annotation, String origin) {
		JsonRegexDesensitize regexAnnotation = null;
		if (annotation != null) {
			regexAnnotation = (JsonRegexDesensitize) annotation;
		}
		RegexDesensitizationTypeEnum type = regexAnnotation.type();
		// 自定义
		if (type.equals(RegexDesensitizationTypeEnum.CUSTOM)) {
			checkValue(regexAnnotation);
			return handle(origin, regexAnnotation.regex(), regexAnnotation.replacement());
		}
		return handle(origin, type);
	}

	default void checkValue(JsonRegexDesensitize annotation) {
		Assert.hasText(annotation.regex(), "The regex of JsonRegexDesensitize can not be empty.");
		Assert.hasText(annotation.replacement(), "The replacement of JsonRegexDesensitize can not be empty.");
	}

}
