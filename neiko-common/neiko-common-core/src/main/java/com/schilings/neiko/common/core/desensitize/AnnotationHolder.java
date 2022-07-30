package com.schilings.neiko.common.core.desensitize;

import com.schilings.neiko.common.core.desensitize.annotation.JsonDesensitize;
import com.schilings.neiko.common.core.desensitize.annotation.JsonRegexDesensitize;
import com.schilings.neiko.common.core.desensitize.annotation.JsonSimpleDesensitize;
import com.schilings.neiko.common.core.desensitize.annotation.JsonSlideDesensitize;
import com.schilings.neiko.common.core.desensitize.handler.DesensitizationHandler;
import com.schilings.neiko.common.core.desensitize.handler.simple.SimpleDesensitizationHandler;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * <pre>
 * <p>注解处理方法 脱敏注解->处理逻辑</p>
 * </pre>
 *
 * @author Schilings
 */
public class AnnotationHolder {

	private AnnotationHolder() {

	}

	/**
	 * 注解类型 处理函数映射
	 */
	private static final Set<Class<? extends Annotation>> ANNOTATION_LIST = new HashSet<>();

	static {
		ANNOTATION_LIST.add(JsonSimpleDesensitize.class);
		ANNOTATION_LIST.add(JsonRegexDesensitize.class);
		ANNOTATION_LIST.add(JsonSlideDesensitize.class);
	}

	/**
	 * 得到当前支持的注解处理类
	 * @return
	 */
	public static Set<Class<? extends Annotation>> getAnnotationClasses() {
		return ANNOTATION_LIST;
	}

}
