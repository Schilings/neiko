package com.schilings.neiko.common.event.handlermapping;




import java.lang.reflect.Method;


public interface EventMappingInfo {

    Class<?> getUserClass();

    Method getMethod();

    String getUniqueName();
    
    
}
