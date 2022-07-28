package com.schilings.neiko.common.redis.message.annoation;


import org.springframework.core.annotation.AliasFor;
import org.springframework.data.redis.listener.Topic;

import java.lang.annotation.*;

/**
 * <pre>
 * <p></p>
 * </pre>
 * @author Schilings
*/
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisListener {


    @AliasFor("topic")
    String value() default "";

    @AliasFor("value")
    String topic() default "";
    

    /**
     * <p>Spring 表达式语言 (SpEL) 表达式用于使事件处理有条件</p>
     * <p>如果表达式的计算结果为布尔值true或以下字符串之一，则将处理该事件： "true" 、 "on" 、 "yes"或"1" 。</p>
     * <p>默认表达式为"" ，表示始终处理事件</p>
     * <p>SpEL 表达式将针对提供以下元数据的专用上下文进行评估：</p>
     * <ul>
     *     <li>#root.topic或topic用于引用{@link Topic}</li>
     *     <li>#root.args或args用于对方法参数数组的引用</li>
     *     <li>方法参数可以通过索引访问。例如，第一个参数可以通过#root.args[0] 、 args[0] 、 #a0或#p0访问。</li>
     *     <li>如果参数名称在编译的字节码中可用，则可以通过名称（带有前面的哈希标记）访问方法参数。</li>
     * </ul>
     * @return
     */
    String condition() default "";


    /**
     * <p>侦听器的可选标识符，默认为声明方法的完全限定签名（例如“mypackage.MyClass.myMethod()”）。</p>
     * @return
     */
    String id() default "";
}
