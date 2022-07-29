package com.schilings.neiko.common.core.desensitize;

import com.schilings.neiko.common.core.desensitize.handler.DesensitizationHandler;
import com.schilings.neiko.common.core.desensitize.handler.regex.DefaultRegexDesensitizationHandler;
import com.schilings.neiko.common.core.desensitize.handler.regex.RegexDesensitizationHandler;
import com.schilings.neiko.common.core.desensitize.handler.simple.SimpleDesensitizationHandler;
import com.schilings.neiko.common.core.desensitize.handler.simple.SixAsteriskDesensitizationHandler;
import com.schilings.neiko.common.core.desensitize.handler.slide.DefaultSlideDesensitizationHandler;
import com.schilings.neiko.common.core.desensitize.handler.slide.SlideDesensitizationHandler;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 脱敏处理器持有者
 * <p>
 * - 默认提供 Regex 和 Slide 类型的脱敏处理器 <br/>
 * - Simple 脱敏处理器则使用SPI方式加载，便于用户扩展处理
 * </p>
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
public final class DesensitizationHandlerHolder {


    private DesensitizationHandlerHolder() {
    }

    private static final Map<Class<? extends DesensitizationHandler>, DesensitizationHandler> MAP = new ConcurrentHashMap<>();

    static {
        // SPI 加载所有的脱敏类型处理
        //暂时不知道为什么不生效
        ServiceLoader<SimpleDesensitizationHandler> loadedDrivers = ServiceLoader.load(SimpleDesensitizationHandler.class);
        for (SimpleDesensitizationHandler desensitizationHandler : loadedDrivers) {
            MAP.put(desensitizationHandler.getClass(), desensitizationHandler);
        }
    }

    /**
     * 获取 DesensitizationHandler
     * @return 处理器实例
     */
    public static DesensitizationHandler getHandler(Class<? extends DesensitizationHandler> handlerClass) {
        if (SimpleDesensitizationHandler.class.isAssignableFrom(handlerClass)) {
            return getSimpleHandler((Class<? extends SimpleDesensitizationHandler>) handlerClass);
        }
        if (RegexDesensitizationHandler.class.isAssignableFrom(handlerClass)) {
            return getRegexHandler((Class<? extends RegexDesensitizationHandler>) handlerClass);
        }
        if (SlideDesensitizationHandler.class.isAssignableFrom(handlerClass)) {
            return getSlideHandler((Class<? extends SlideDesensitizationHandler>) handlerClass);
        }
        return MAP.get(handlerClass);
    }
    
    
    /**
     * 获取指定的 SlideDesensitizationHandler
     * @param handlerClass SlideDesensitizationHandler的实现类
     * @return 处理器实例
     */
    public static SlideDesensitizationHandler getSlideHandler(
            Class<? extends SlideDesensitizationHandler> handlerClass) {
        return (SlideDesensitizationHandler) MAP.computeIfAbsent(handlerClass, key -> new DefaultSlideDesensitizationHandler());
    }
    
    /**
     * 获取指定的 RegexDesensitizationHandler
     * @param handlerClass RegexDesensitizationHandler的实现类
     * @return 处理器实例
     */
    public static RegexDesensitizationHandler getRegexHandler(
            Class<? extends RegexDesensitizationHandler> handlerClass) {
        return (RegexDesensitizationHandler) MAP.computeIfAbsent(handlerClass, key -> new DefaultRegexDesensitizationHandler());
    }

    /**
     * 获取指定的 SimpleDesensitizationHandler
     * @param handlerClass SimpleDesensitizationHandler的实现类
     * @return 处理器实例
     */
    public static SimpleDesensitizationHandler getSimpleHandler(
            Class<? extends SimpleDesensitizationHandler> handlerClass) {
        return (SimpleDesensitizationHandler) MAP.computeIfAbsent(handlerClass, key -> new SixAsteriskDesensitizationHandler());
    }

    /**
     * 添加Handler
     * @param handlerClass DesensitizationHandler的实现类
     * @param handler 处理器实例
     * @return handler 处理器实例
     */
    public static DesensitizationHandler addHandler(Class<? extends DesensitizationHandler> handlerClass,
                                                    DesensitizationHandler handler) {
        return MAP.put(handlerClass, handler);
    }
}
