package com.schilings.neiko.common.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.lang.reflect.Type;
import java.util.function.Consumer;

public class JacksonAdapter implements JsonUtils.Adapter {

	@Getter
	@Setter
	static ObjectMapper mapper = new ObjectMapper();

	static {
		// 确定遇到未知属性（不映射到属性的属性，并且没有“任何设置器”或可以处理它的处理程序）是否应导致失败（通过抛出JsonMappingException ）的功能
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	public void config(Consumer<ObjectMapper> consumer) {
		consumer.accept(mapper);
	}

	@SneakyThrows(JsonProcessingException.class)
	@Override
	public String toJson(Object obj) {
		return mapper.writeValueAsString(obj);
	}

	@SneakyThrows({ JsonMappingException.class, JsonProcessingException.class })
	@Override
	public <T> T toObj(String json, Class<T> r) {
		return mapper.readValue(json, r);
	}

	@SneakyThrows({ JsonMappingException.class, JsonProcessingException.class })
	@Override
	public <T> T toObj(String json, Type t) {
		return mapper.readValue(json, mapper.constructType(t));
	}

	@SneakyThrows({ JsonMappingException.class, JsonProcessingException.class })
	@Override
	public <T> T toObj(String json, TypeReference<T> t) {
		return mapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<T>() {
			@Override
			public Type getType() {
				return t.getType();
			}
		});
	}
}
