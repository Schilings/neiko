package com.schilings.neiko.common.core.desensitize.handler.simple;


import com.schilings.neiko.common.core.desensitize.handler.DesensitizationHandler;

import java.lang.annotation.Annotation;

public interface SimpleDesensitizationHandler extends DesensitizationHandler {



    String handle(String orgin);

    @Override
    default String handle(Annotation annotation, String orgin){
        return handle(orgin);
    }


}
