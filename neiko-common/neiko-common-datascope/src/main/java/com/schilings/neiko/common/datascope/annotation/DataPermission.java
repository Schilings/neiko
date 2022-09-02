package com.schilings.neiko.common.datascope.annotation;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {


    /**
     * 当前类或方法是否忽略数据权限
     * @return boolean 默认返回 false
     */
    boolean ignore() default false;

    /**
     * 仅对指定资源类型进行数据权限控制，只在开启情况下有效，当该数组有值时，exclude不生效
     * @see DataPermission#excludeResources
     * @return 资源类型数组
     */
    String[] includeResources() default {};

    /**
     * 对指定资源类型跳过数据权限控制，只在开启情况下有效，当该includeResources有值时，exclude不生效
     * @see DataPermission#includeResources
     * @return 资源类型数组
     */
    String[] excludeResources() default {};

}
