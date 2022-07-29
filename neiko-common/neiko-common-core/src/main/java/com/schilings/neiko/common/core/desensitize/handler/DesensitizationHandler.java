package com.schilings.neiko.common.core.desensitize.handler;

/**
 * <pre>
 * <p>脱敏处理器</p>
 * </pre>
 * @author Schilings
*/
public interface DesensitizationHandler {


    /**
     * 脱敏函数
     * @param value
     * @return
     */
    String desensitize(String value);
}
