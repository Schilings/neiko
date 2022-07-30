package com.schilings.neiko.common.cache.annotation;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.SpringCacheAnnotationParser;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * <p>指示调用方法（或类中的所有方法）的结果可以被缓存的注解。</p>
 * <p>{@link Cacheable}</p>
 * <p>{@link SpringCacheAnnotationParser}</p>
 * </pre>
 * @author Schilings
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface NeikoCacheable {

	/**
	 * 缓存存储于哪个仓库
	 */
	String cacheRepository() default "local";

	/**
	 * 用于动态计算密钥的 Spring 表达式语言 (SpEL) 表达式。
	 * <p>
	 * 默认为"" ， {@code ""}, 这意味着所有方法参数都被视为一个键，除非配置了自定义{@link #keyGenerator}
	 * <p>
	 * SpEL 表达式根据提供以下元数据的专用上下文进行评估：
	 * <ul>
	 * <li>{@code #root.method}, {@code #root.target}, {@code #root.caches}分别用于对
	 * {@link java.lang.reflect.Method method}, 目标对象和受影响缓存的引用。</li>
	 * <li>方法名称 ({@code #root.methodName})和目标类({@code #root.targetClass}) 的快捷方式也可用。
	 * <li>方法参数可以通过索引访问。例如，可以通过{@code #root.args[1]}, {@code #p1} 或 {@code #a1}.
	 * 访问第二个参数。如果该信息可用，也可以按名称访问参数</li>
	 * </ul>
	 */
	String key() default "";

	String keyGenerator() default "";

	/**
	 * 有效时间(S) ttl = 0 使用全局配置值 ttl < 0 : 不超时 ttl > 0 : 使用此超时间
	 */
	long ttl() default -1;

	/**
	 * 控制时长单位，默认为 SECONDS 分钟
	 * @return
	 */
	TimeUnit unit() default TimeUnit.SECONDS;

	/**
	 * Spring 表达式语言 (SpEL) 表达式用于使方法缓存有条件。
	 * <p>
	 * 默认为"" ， {@code ""},表示方法结果始终被缓存。
	 * <p>
	 * SpEL 表达式根据提供以下元数据的专用上下文进行评估：
	 * <ul>
	 * <li>{@code #root.method}, {@code #root.target}, {@code #root.caches}分别用于对
	 * {@link java.lang.reflect.Method method}, 目标对象和受影响缓存的引用。</li>
	 * <li>方法名称 ({@code #root.methodName})和目标类({@code #root.targetClass}) 的快捷方式也可用。
	 * <li>方法参数可以通过索引访问。例如，可以通过{@code #root.args[1]}, {@code #p1} 或 {@code #a1}.
	 * 访问第二个参数。如果该信息可用，也可以按名称访问参数</li>
	 * </ul>
	 */
	String condition() default "";

	/**
	 * <p>
	 * 用于否决方法缓存的 Spring 表达式语言 (SpEL) 表达式。
	 * <p>
	 * 与{@link #condition}不同，此表达式在方法被调用后计算，因此可以引用{@code result}.
	 * <p>
	 * 默认为"" ， {@code ""},这意味着缓存永远不会被否决
	 * <p>
	 * SpEL 表达式根据提供以下元数据的专用上下文进行评估：
	 * <ul>
	 * <li>{@code #root.method}, {@code #root.target}, {@code #root.caches}分别用于对
	 * {@link java.lang.reflect.Method method}, 目标对象和受影响缓存的引用。</li>
	 * <li>方法名称 ({@code #root.methodName})和目标类({@code #root.targetClass}) 的快捷方式也可用。
	 * <li>方法参数可以通过索引访问。例如，可以通过{@code #root.args[1]}, {@code #p1} 或 {@code #a1}.
	 * 访问第二个参数。如果该信息可用，也可以按名称访问参数</li>
	 * </ul>
	 */
	String unless() default "";

	/**
	 * Synchronize the invocation of the underlying method if several threads are
	 * attempting to load a value for the same key. The synchronization leads to a couple
	 * of limitations:
	 * <ol>
	 * <li>{@link #unless()} is not supported</li>
	 * <li>Only one cache may be specified</li>
	 * <li>No other cache-related operation can be combined</li>
	 * </ol>
	 * This is effectively a hint and the actual cache provider that you are using may not
	 * support it in a synchronized fashion. Check your provider documentation for more
	 * details on the actual semantics.
	 * @since 4.3
	 * @see org.springframework.cache.Cache#get(Object, Callable)
	 */
	boolean sync() default false;

}
