package com.schilings.neiko.common.event.handler;


import org.springframework.web.method.HandlerMethod;

/**
 * <pre>{@code
 *      
 * }
 * <p>对事件处理器的适配类</p>
 * </pre>
 * @author Schilings
*/
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
            method.doInvoke(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



  
    
}
