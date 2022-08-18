package com.schilings.neiko.common.netty.core;

/**
 * 
 * <p>无栈异常</p>
 * 
 * @author Schilings
*/
public class NoStackTraceThrowable extends Throwable {

    public NoStackTraceThrowable(String message) {
        super(message, null, false, false);
    }
}
