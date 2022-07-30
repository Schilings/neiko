package com.schilings.neiko.common.cache.components;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

public interface KeyGenerator {

	// String generate(String keyPrefix, String keyJoint);
	//
	// String generate(AnnotatedElement annotatedElement);

	String generate(Object target, Method method, Object... params);

}
