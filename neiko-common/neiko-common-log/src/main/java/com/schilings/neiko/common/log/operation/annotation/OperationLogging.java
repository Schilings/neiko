package com.schilings.neiko.common.log.operation.annotation;


import com.schilings.neiko.common.log.operation.enums.OperationTypeEnum;

import java.lang.annotation.*;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLogging {

    /**
     * 日志信息
     * @return 日志描述信息
     */
    String msg() default "";

    /**
     * 日志操作类型
     * @return 日志操作类型枚举
     */
    OperationTypeEnum type();

    /**
     * 是否保存方法入参
     * @return boolean
     */
    boolean recordParams() default true;

    /**
     * 是否保存方法返回值
     * @return boolean
     */
    boolean recordResult() default true;

}
