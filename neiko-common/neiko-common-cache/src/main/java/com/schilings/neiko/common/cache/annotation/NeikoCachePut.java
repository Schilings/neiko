package com.schilings.neiko.common.cache.annotation;


import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target({ElementType.TYPE, ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface NeikoCachePut {


    /**
     * 缓存存储于哪个仓库
     */
    String cacheRepository() default "simple";

    /**
     * 用于动态计算密钥的 Spring 表达式语言 (SpEL) 表达式。
     * <p>默认为"" ， {@code ""}, 这意味着所有方法参数都被视为一个键，除非配置了自定义{@link #keyGenerator}
     * <p>SpEL 表达式根据提供以下元数据的专用上下文进行评估：
     * <ul>
     * <li>{@code #root.method}, {@code #root.target},  {@code #root.caches}分别用于对
     * {@link java.lang.reflect.Method method}, 目标对象和受影响缓存的引用。</li>
     * <li>方法名称  ({@code #root.methodName})和目标类({@code #root.targetClass}) 的快捷方式也可用。
     * <li>方法参数可以通过索引访问。例如，可以通过{@code #root.args[1]}, {@code #p1} 或 {@code #a1}.
     * 访问第二个参数。如果该信息可用，也可以按名称访问参数</li>
     * </ul>
     */
    String key() default "";

    String keyGenerator() default "";

    /**
     * Spring 表达式语言 (SpEL) 表达式用于使方法缓存有条件。
     * <p>默认为"" ， {@code ""},表示方法结果始终被缓存。
     * <p>SpEL 表达式根据提供以下元数据的专用上下文进行评估：
     * <ul>
     * <li>{@code #root.method}, {@code #root.target},  {@code #root.caches}分别用于对
     * {@link java.lang.reflect.Method method}, 目标对象和受影响缓存的引用。</li>
     * <li>方法名称  ({@code #root.methodName})和目标类({@code #root.targetClass}) 的快捷方式也可用。
     * <li>方法参数可以通过索引访问。例如，可以通过{@code #root.args[1]}, {@code #p1} 或 {@code #a1}.
     * 访问第二个参数。如果该信息可用，也可以按名称访问参数</li>
     * </ul>
     */
    String condition() default "";
    
    
}
