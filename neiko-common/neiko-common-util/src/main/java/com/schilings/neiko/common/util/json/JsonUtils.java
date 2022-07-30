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

	public void switchAdapter(JsonToolSouce souce) {
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

	/**
	 * @author lingting 2021/2/25 20:38
	 */
	private static boolean classIsPresent(String className) {
		return ClassUtils.isPresent(className, JsonUtils.class.getClassLoader());
	}

	public interface Adapter {

		String toJson(Object obj);

		<T> T toObj(String jsonStr, Class<T> c);
		
		<T> T toObj(String jsonStr, Type c);


	}

}
