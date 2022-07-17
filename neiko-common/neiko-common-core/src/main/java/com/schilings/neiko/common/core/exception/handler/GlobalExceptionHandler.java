package com.schilings.neiko.common.core.exception.handler;


/**
 * <pre>{@code
 *      
 * }
 * <p>全局异常处理类</p>
 * </pre>
 * @author Schilings
*/
public class GlobalExceptionHandler implements ExceptionHandler {
    
    @Override
    public void handle(Throwable throwable) {
        handleInternal(throwable);
    }

    public void handleInternal(Throwable throwable) {
        
    }
}
