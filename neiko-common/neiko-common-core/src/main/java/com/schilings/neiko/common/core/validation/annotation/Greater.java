package com.schilings.neiko.common.core.validation.annotation;

import com.schilings.neiko.common.core.validation.validator.*;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <pre>
 * <p>校验数值是否大于等于设定值</p>
 * </pre>
 * @author Schilings
*/
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(Greater.List.class)
@Constraint(validatedBy = {
        GreaterValidatorForBigDecimal.class, 
        GreaterValidatorForBigInteger.class, 
        GreaterValidatorForByte.class, 
        GreaterValidatorForCharSequence.class,
        GreaterValidatorForDouble.class,
        GreaterValidatorForFloat.class,
        GreaterValidatorForInteger.class,
        GreaterValidatorForLong.class,
        GreaterValidatorForNumber.class,
        GreaterValidatorForShort.class})
public @interface Greater {


    /**
     * 提示内容,必须在指定范围中
     */
    String message() default "value is not greater or equal to {value}";

    /**
     * 要比较的值
     * @return
     */
    long value();
    
    /**
     * 允许相等情况 , 默认包括相等
     */
    boolean equal() default true;

    /**
     * 分组
     */
    Class<?>[] groups() default { };

    /**
     * Payload 数组
     */
    Class<? extends Payload>[] payload() default { };
    
    /**
     * 在同一元素上定义多个@Better约束。
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Greater[] value();
    }
    
}
