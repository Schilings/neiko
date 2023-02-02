package com.schilings.neiko.sample.resource.server.http;


import org.springframework.util.StringValueResolver;

/**
 * 用于解析 接口或者类上的@HttpExchange的url属性
 */
public class CustomStringValueResolver implements StringValueResolver {
    @Override
    public String resolveStringValue(String strVal) {
        return strVal;
    }
}
