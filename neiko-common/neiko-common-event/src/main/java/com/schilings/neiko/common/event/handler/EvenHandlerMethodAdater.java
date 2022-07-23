package com.schilings.neiko.common.event.handler;


import lombok.extern.slf4j.Slf4j;

/**
 * <pre>{@code
 *      
 * }
 * <p>对事件处理器的适配类</p>
 * </pre>
 * @author Schilings
*/
@Slf4j
public class EvenHandlerMethodAdater implements EventHandler {

    private final HandlerMethod handlerMethod;

    public EvenHandlerMethodAdater(HandlerMethod handlerMethod) {
        this.handlerMethod = handlerMethod.createWithResolvedBean();
    }

    @Override
    public void handle(Object event) {
        handleInternal(event,  handlerMethod);
        
    }

    protected void handleInternal(Object event, HandlerMethod handlerMethod){
        invokeHandlerMethod(event, handlerMethod);
    }

    private void invokeHandlerMethod(Object event, HandlerMethod handlerMethod) {
        EventHandlerMethod method = new EventHandlerMethod(handlerMethod);
        try {
            method.invokeForEvent(event);
        } catch (Exception e) {
            log.error("EventHandlerMethod executing throw ex:{}", e.getMessage());
            log.error("{}", e);
            
        }

    }

    
}
