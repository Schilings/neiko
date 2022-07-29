package com.schilings.neiko.common.core.desensitize.handler.simple;


public interface SimpleDesensitizationHandler {

    /**
     * 简单脱敏处理
     * @param orgin 脱敏前
     * @return 脱敏后
     */
    String handle(String orgin);
}
