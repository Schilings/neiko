package com.schilings.neiko.common.core.desensitize.handler;

import java.lang.annotation.Annotation;

/**
 * <pre>
 * <p>脱敏处理器</p>
 * </pre>
 *
 * @author Schilings
 */
public interface DesensitizationHandler {

	/**
	 * 脱敏函数
	 * @return
	 */
	String handle(Annotation annotation, String value);

}
