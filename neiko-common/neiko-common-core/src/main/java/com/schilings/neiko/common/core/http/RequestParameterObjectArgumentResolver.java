package com.schilings.neiko.common.core.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.service.invoker.HttpRequestValues;
import org.springframework.web.service.invoker.HttpServiceArgumentResolver;

import java.util.Map;
import java.util.Optional;

public class RequestParameterObjectArgumentResolver implements HttpServiceArgumentResolver {

	protected final Log logger = LogFactory.getLog(getClass());

	private static final TypeReference<Map<String, String>> MAP_OBJECT_TYPE = new TypeReference<>() {
	};

	private final ObjectMapper objectMapper;

	public RequestParameterObjectArgumentResolver() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModules(new JavaTimeModule());
	}

	public RequestParameterObjectArgumentResolver(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public boolean resolve(Object argument, MethodParameter parameter, HttpRequestValues.Builder requestValues) {
		RequestParameterObject annot = parameter.getParameterAnnotation(RequestParameterObject.class);
		if (annot == null) {
			return false;
		}
		if (argument == null) {
			return true;
		}
		Map<String, String> paramsMap = parseMap(argument);
		for (Map.Entry<String, ?> entry : paramsMap.entrySet()) {
			addSingleValue(entry.getKey(), entry.getValue(), false, null, "request paramter object", requestValues);
		}
		return true;
	}

	private Map<String, String> parseMap(Object argument) {
		return objectMapper.convertValue(argument, MAP_OBJECT_TYPE);
	}

	private void addSingleValue(String name, @Nullable Object value, boolean required, @Nullable Object defaultValue,
			String valueLabel, HttpRequestValues.Builder requestValues) {

		if (value instanceof Optional<?> optionalValue) {
			value = optionalValue.orElse(null);
		}

		if (value == null && defaultValue != null) {
			value = defaultValue;
		}

		if (value == null) {
			Assert.isTrue(!required, () -> "Missing " + valueLabel + " value '" + name + "'");
			return;
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Resolved " + valueLabel + " value '" + name + ":" + value + "'");
		}

		addRequestValue(name, value, requestValues);
	}

	private void addRequestValue(String name, Object value, HttpRequestValues.Builder requestValues) {

		requestValues.addRequestParameter(name, (String) value);
	}

}
