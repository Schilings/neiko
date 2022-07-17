package com.schilings.neiko.common.util.json;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <pre>{@code
 *      
 * }
 * <p></p>
 * </pre>
 * @author Schilings
*/
@Getter
@AllArgsConstructor
public enum JsonToolSouce {

    JACKSON(com.fasterxml.jackson.databind.ObjectMapper.class),
    GSON(com.google.gson.Gson.class),
    FAST_JSON(com.alibaba.fastjson.JSON.class);

    private Class clz;
}
