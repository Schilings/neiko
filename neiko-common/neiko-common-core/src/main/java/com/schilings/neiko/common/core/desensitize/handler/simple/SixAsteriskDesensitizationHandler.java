package com.schilings.neiko.common.core.desensitize.handler.simple;


public class SixAsteriskDesensitizationHandler implements SimpleDesensitizationHandler{

    /**
     * 简单返回6个*
     * @param orgin 脱敏前
     * @return
     */
    @Override
    public String handle(String orgin) {
        return "******";
    }
}
