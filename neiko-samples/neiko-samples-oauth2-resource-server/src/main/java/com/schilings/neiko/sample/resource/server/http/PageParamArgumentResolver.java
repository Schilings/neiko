package com.schilings.neiko.sample.resource.server.http;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schilings.neiko.common.model.domain.PageParam;
import org.springframework.core.MethodParameter;
import org.springframework.web.service.invoker.HttpRequestValues;
import org.springframework.web.service.invoker.HttpServiceArgumentResolver;

import java.util.Map;


public class PageParamArgumentResolver implements HttpServiceArgumentResolver {

    private static final TypeReference<Map<String, Object>> MAP_OBJECT_TYPE = new TypeReference<>() {};
    @Override
    public boolean resolve(Object argument, MethodParameter parameter, HttpRequestValues.Builder requestValues) {

        if (!parameter.getParameterType().equals(PageParam.class)) {
            return false;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        PageParam pageParam = null;
        if (argument == null) {
            pageParam = new PageParam();
        }
        if (argument instanceof PageParam param) {
            pageParam = param;
        }

        Map<String, Object> paramsMap = objectMapper.convertValue(pageParam, MAP_OBJECT_TYPE);

        requestValues.addRequestParameter("page", String.valueOf(pageParam.getPage()));
        requestValues.addRequestParameter("size", String.valueOf(pageParam.getSize()));
        
        return true;
    }
    
}
