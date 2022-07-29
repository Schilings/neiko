package com.schilings.neiko.common.core.desensitize;


import com.fasterxml.jackson.databind.module.SimpleModule;
import com.schilings.neiko.common.core.desensitize.serializer.JsonDesensitizeSerializerModifier;


/**
 * Json 脱敏模块
 *
 * @author hccake
 */
public class JsonDesensitizeModule extends SimpleModule {

    public JsonDesensitizeModule(JsonDesensitizeSerializerModifier jsonDesensitizeSerializerModifier) {
        super();
        this.setSerializerModifier(jsonDesensitizeSerializerModifier);
    }

}