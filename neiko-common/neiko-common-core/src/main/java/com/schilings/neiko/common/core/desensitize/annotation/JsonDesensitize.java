package com.schilings.neiko.common.core.desensitize.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.schilings.neiko.common.core.desensitize.enums.DesensitizationType;

import java.lang.annotation.*;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author Schilings
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside//Jackson识别
@JsonSerialize
public @interface JsonDesensitize {


    /**
     * 条件匹配下才进行脱敏
     */
    String condition() default "";

    /**
     * 脱敏类型
     */
    DesensitizationType type() default DesensitizationType.NONE;





}
