package com.schilings.neiko.common.util.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author lingting 2021/2/26 10:32
 */
public class FastjsonAdapter implements JsonUtils.Adapter {

	/**
	 * json str -> obj 时
	 */
	static final List<Feature> FEATURES = new ArrayList<>(Feature.values().length);

	/**
	 * obj -> json str
	 */
	static final List<SerializerFeature> SERIALIZER_FEATURES = new ArrayList<>(SerializerFeature.values().length);

	private static Feature[] features = new Feature[0];

	private static SerializerFeature[] serializerFeatures = new SerializerFeature[0];

	/**
	 * 不要使用 config 以外的形式更新配置
	 */
	public void config(BiConsumer<List<Feature>, List<SerializerFeature>> consumer) {
		consumer.accept(FEATURES, SERIALIZER_FEATURES);
		features = FEATURES.toArray(new Feature[0]);
		serializerFeatures = SERIALIZER_FEATURES.toArray(new SerializerFeature[0]);
	}

	@Override
	public String toJson(Object obj) {
		return JSON.toJSONString(obj, serializerFeatures);
	}

	@Override
	public <T> T toObj(String json, Class<T> r) {
		return JSON.parseObject(json, r, features);
	}

	@Override
	public <T> T toObj(String json, Type t) {
		return JSON.parseObject(json, t, features);
	}

}
