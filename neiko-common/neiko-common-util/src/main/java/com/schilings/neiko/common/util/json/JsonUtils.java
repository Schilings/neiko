package com.schilings.neiko.common.util.json;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <pre>{@code
 *
 * }
 * <p>Json工具类</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

	private static final String JACKSON_CLASS = "com.fasterxml.jackson.databind.ObjectMapper";

	private static final String GSON_CLASS = "com.google.gson.Gson";

	private static final String FAST_JSON_CLASS = "com.alibaba.fastjson.JSON";

	private static Map<JsonToolSouce, Adapter> adapterMappings;

	@Getter
	private static Adapter jsonAdapter;

	static {
		if (classIsPresent(JACKSON_CLASS)) {
			jsonAdapter = new JacksonAdapter();
		}
		else if (classIsPresent(GSON_CLASS)) {
			jsonAdapter = new GsonAdapter();
		}
		else if (classIsPresent(FAST_JSON_CLASS)) {
			jsonAdapter = new FastjsonAdapter();
		}

	}

	public static void switchAdapter(JsonToolSouce souce) {
		if (Objects.isNull(adapterMappings)) {
			adapterMappings = new HashMap<>(5);
			adapterMappings.put(JsonToolSouce.JACKSON, new JacksonAdapter());
			adapterMappings.put(JsonToolSouce.GSON, new GsonAdapter());
		}
		jsonAdapter = adapterMappings.get(souce);
	}

	public static String toJson(Object obj) {
		return jsonAdapter.toJson(obj);
	}

	public static <T> T toObj(String json, Class<T> r) {
		return jsonAdapter.toObj(json, r);
	}

	public static <T> T toObj(String json, TypeReference<T> t) {
		return jsonAdapter.toObj(json, t);
	}

	public static <T> T toObj(String json, Type t) {
		// 防止误传入其他类型的 typeReference 走这个方法然后转换出错
		if (classIsPresent(FAST_JSON_CLASS) && t instanceof com.alibaba.fastjson.TypeReference) {
			return toObj(json, new TypeReference<T>() {
				@Override
				public Type getType() {
					return ((com.alibaba.fastjson.TypeReference<?>) t).getType();
				}
			});
		}
		else if (classIsPresent(JACKSON_CLASS) && t instanceof com.fasterxml.jackson.core.type.TypeReference) {
			return toObj(json, new TypeReference<T>() {
				@Override
				public Type getType() {
					return ((com.fasterxml.jackson.core.type.TypeReference<?>) t).getType();
				}
			});
		}

		return jsonAdapter.toObj(json, t);
	}

	/**
	 * @author lingting 2021/2/25 20:38
	 */
	private static boolean classIsPresent(String className) {
		return ClassUtils.isPresent(className, JsonUtils.class.getClassLoader());
	}

	public interface Adapter {

		/**
		 * obj -> jsonStr
		 * @param obj obj
		 * @return java.lang.String
		 * @author lingting 2021-02-25 21:00
		 */
		String toJson(Object obj);

		/**
		 * jsonStr -> obj
		 * @param json json str
		 * @param r obj.class
		 * @return T
		 * @author lingting 2021-02-25 21:02
		 */
		<T> T toObj(String json, Class<T> r);

		/**
		 * jsonStr -> obj
		 * @param json json str
		 * @param t (obj.class)type
		 * @return T
		 * @author lingting 2021-02-25 21:02
		 */
		<T> T toObj(String json, Type t);

		/**
		 *
		 * jsonStr -> obj
		 * @param json json str
		 * @param t TypeReference
		 * @return T
		 * @author lingting 2021-02-25 21:49
		 */
		<T> T toObj(String json, TypeReference<T> t);

	}

}
