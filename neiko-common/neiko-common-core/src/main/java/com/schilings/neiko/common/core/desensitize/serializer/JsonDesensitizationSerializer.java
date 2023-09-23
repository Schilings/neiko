package com.schilings.neiko.common.core.desensitize.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.schilings.neiko.common.core.desensitize.DesensitizationExpressionEvaluator;
import com.schilings.neiko.common.core.desensitize.DesensitizationHandlerHolder;
import com.schilings.neiko.common.core.desensitize.handler.DesensitizationHandler;
import lombok.SneakyThrows;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * <pre>
 * <p>Jackson脱敏处理序列化器</p>
 * </pre>
 *
 * @author Schilings
 */
public class JsonDesensitizationSerializer extends JsonSerializer<Object> implements DesensitizationSerializer {

	private final Annotation jsonDesensitizeAnnotation;

	private final Map<String, Object> annotationAttributes;

	private final DesensitizationHandler handler;

	private final BeanDescription description;

	private final BeanFactory beanFactory;

	private final DesensitizationExpressionEvaluator evaluator = new DesensitizationExpressionEvaluator();

	public JsonDesensitizationSerializer(Annotation jsonDesensitizeAnnotation, BeanDescription description,
			BeanFactory beanFactory) {
		Assert.notNull(jsonDesensitizeAnnotation, "JsonDesensitizeAnnotation can not be null");
		this.jsonDesensitizeAnnotation = jsonDesensitizeAnnotation;
		this.annotationAttributes = AnnotationUtils.getAnnotationAttributes(jsonDesensitizeAnnotation);

		Class handlerClass = (Class) this.annotationAttributes.get("handlerClass");
		Assert.notNull(handlerClass, "Handler Class of JsonDesensitizeAnnotation must not be null");

		this.handler = DesensitizationHandlerHolder.getHandler(handlerClass);
		Assert.notNull(this.handler, "DesensitizationHandler of JsonDesensitizeAnnotation must not be null");

		this.description = description;
		this.beanFactory = beanFactory;

	}

	@SneakyThrows
	@Override
	public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializers) {
		if (value instanceof String str) {
			// 字段名
			String fieldName = jsonGenerator.getOutputContext().getCurrentName();
			AnnotatedElementKey elementKey = new AnnotatedElementKey(
					description.getBeanClass().getDeclaredField(fieldName), description.getBeanClass());
			if (conditionPass(str, elementKey)) {
				jsonGenerator.writeString(handler.handle(jsonDesensitizeAnnotation, str));
				return;
			}
			// 未开启脱敏
			jsonGenerator.writeString(str);
		}
	}

	public boolean conditionPass(Object value, AnnotatedElementKey elementKey) {
		String condition = getCondition();
		if (StringUtils.hasText(condition)) {
			Assert.notNull(this.evaluator, "DesensitizationExpressionEvaluator must not be null");
			return this.evaluator.condition(condition, value, elementKey, beanFactory);
		}
		return true;
	}

	public String getCondition() {
		return (String) this.annotationAttributes.get("condition");
	}

}
