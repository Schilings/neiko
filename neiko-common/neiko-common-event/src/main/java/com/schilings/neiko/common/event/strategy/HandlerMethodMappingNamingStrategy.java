package com.schilings.neiko.common.event.strategy;

/**
 * <pre>{@code
 *      
 * }
 * <p>为处理程序方法的映射分配名称的策略。</p>
 * </pre>
 * @author Schilings
*/
public interface HandlerMethodMappingNamingStrategy {


    /**
     * 确定给定 HandlerMethod 和映射的名称。
     *
     * @param handlerMethod
     * @param mapping
     * @return
     */
    //String getName(HandlerMethod handlerMethod, T mapping);
    String getName(Object o);

}
