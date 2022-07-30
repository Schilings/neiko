package com.schilings.neiko.common.core.desensitize.serializer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import com.schilings.neiko.common.core.desensitize.AnnotationHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;

import java.lang.annotation.Annotation;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JsonDesensitizeSerializerModifier extends BeanSerializerModifier {

	private final BeanFactory beanFactory;

	@Override
	public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
			List<BeanPropertyWriter> beanProperties) {
		for (BeanPropertyWriter property : beanProperties) {
			// 取得属性上的注解
			Annotation annotation = getDesensitizeAnnotation(property);
			// 字符串类型
			if (annotation != null && property.getType().isTypeOrSubTypeOf(String.class)) {
				// 分配序列化处理器
				property.assignSerializer(new JsonDesensitizationSerializer(annotation, beanDesc, beanFactory));
			}

		}
		return beanProperties;
	}

	/**
	 * 得到脱敏注解
	 * @param beanProperty BeanPropertyWriter
	 * @return 返回第一个获取的脱敏注解
	 */
	private Annotation getDesensitizeAnnotation(BeanPropertyWriter beanProperty) {
		for (Class<? extends Annotation> annotationClass : AnnotationHolder.getAnnotationClasses()) {
			Annotation annotation = beanProperty.getAnnotation(annotationClass);
			if (annotation != null) {
				return annotation;
			}
		}
		return null;
	}

}
