package com.schilings.neiko.sample.resource.server.http;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schilings.neiko.authorization.model.qo.OAuth2RegisteredClientQO;

import org.springframework.core.MethodParameter;
import org.springframework.web.service.invoker.HttpRequestValues;
import org.springframework.web.service.invoker.HttpServiceArgumentResolver;

import java.util.Map;

public class RequestParamObjectArgumentResolver implements HttpServiceArgumentResolver {

    private static final TypeReference<Map<String, String>> MAP_OBJECT_TYPE = new TypeReference<>() {};
    @Override
    public boolean resolve(Object argument, MethodParameter parameter, HttpRequestValues.Builder requestValues) {
        if (!parameter.getParameterType().equals(OAuth2RegisteredClientQO.class)) {
            return false;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        OAuth2RegisteredClientQO paramObject = null;
        if (argument == null) {
            paramObject = new OAuth2RegisteredClientQO();
        }
        if (argument instanceof OAuth2RegisteredClientQO param) {
            paramObject = param;
        }

        Map<String, String> paramsMap = objectMapper.convertValue(paramObject, MAP_OBJECT_TYPE);
        paramsMap.forEach((key, value) -> {
            requestValues.addRequestParameter(key, (String) value);
        });

        return true;
    }
}
