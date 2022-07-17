package com.schilings.neiko.common.core.validation.annotation;

import com.schilings.neiko.common.core.validation.validator.ValueInEnumValidator;

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
 * @author housl
 * @version 1.0
 */
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(ValueInEnum.List.class)
@Constraint(validatedBy = {ValueInEnumValidator.class })
public @interface ValueInEnum {


    /**
     * 提示内容,必须在指定范围中
     */
    String message() default "value is not in enum {enumClass}";

    /**
     * 指定的枚举类型
     */
    Class<?>[] enumClass();

    /**
     * 枚举类型用于取值的静态方法，即返回用于校验有效范围集合的方法
     * 默认的valueOf方法，只能通过枚举类型名传参,例如
     * <pre>
     *     {@code 
     *     public static XXX valueOf(String name);
     *     
     *     @ValueInEnum(enumClass = XXXX.class)
     *     private String name;
     *     }
     * </pre>
     * 这样只能校验name是不是与XXXX的其中一个枚举同名
     */
    String method() default "valueOf";

    /**
     * 允许值为 null, 默认不允许
     */
    boolean allowNull() default false;
    
    /**
     * 分组
     */
    Class<?>[] groups() default { };

    /**
     * Payload 数组
     */
    Class<? extends Payload>[] payload() default { };
    
    
    /**
     * 在同一元素上定义多个@ValueInEnum约束。
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ValueInEnum[] value();
    }
}
