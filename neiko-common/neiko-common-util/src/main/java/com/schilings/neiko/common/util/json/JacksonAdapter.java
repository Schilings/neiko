package com.schilings.neiko.common.util.json;


public class JacksonAdapter implements JsonUtils.Adapter{
    @Override
    public String toJson(Object obj) {
        return null;
    }

    @Override
    public <T> T toObj(String jsonStr, Class<T> c) {
        return null;
    }

}
