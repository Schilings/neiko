package com.schilings.neiko.common.core.desensitize.handler.slide;


import com.schilings.neiko.common.core.desensitize.annotation.JsonSlideDesensitize;
import com.schilings.neiko.common.core.desensitize.enums.SlideDesensitizationTypeEnum;
import com.schilings.neiko.common.core.desensitize.handler.DesensitizationHandler;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;

public interface SlideDesensitizationHandler extends DesensitizationHandler {

    String handle(String origin, int leftPlainTextLen, int rightPlainTextLen, String maskString);

    default String handle(String origin, SlideDesensitizationTypeEnum type) {
        return handle(origin, type.getLeftPlainTextLen(), type.getRightPlainTextLen(), type.getMaskString());
    }
    
    @Override
    default String handle(Annotation annotation, String origin) {
        JsonSlideDesensitize slideAnnotation = null;
        if (annotation != null) {
            slideAnnotation = (JsonSlideDesensitize) annotation;
        }
        SlideDesensitizationTypeEnum type = slideAnnotation.type();
        ////自定义
        if (type.equals(SlideDesensitizationTypeEnum.CUSTOM)) {
            checkValue(slideAnnotation);
            return handle(origin, slideAnnotation.leftPlainTextLen(), slideAnnotation.rightPlainTextLen(), slideAnnotation.maskString());
        }
        return handle(origin, type);
    }

    default void checkValue(JsonSlideDesensitize annotation) {
        Assert.state(annotation.leftPlainTextLen() >= 0,"The leftPlainTextLen of JsonSlideDesensitize can not be less than zeros.");
        Assert.state(annotation.rightPlainTextLen() >= 0,"The rightPlainTextLen of JsonSlideDesensitize can not be less than zeros.");
        Assert.hasText(annotation.maskString(),"The maskString of JsonSlideDesensitize can not be empty.");
    }
}
