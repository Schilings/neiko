package com.schilings.neiko.common.redis.message;

import com.schilings.neiko.common.redis.RedisHelper;
import com.schilings.neiko.common.redis.message.annoation.RedisListener;
import com.schilings.neiko.common.redis.message.evaluator.MessageEventExpressionEvaluator;
import com.schilings.neiko.common.redis.message.factory.MessageEventListenerFactory;
import com.schilings.neiko.common.redis.message.listener.MessageEventListener;
import com.schilings.neiko.common.redis.message.listener.MessageEventListenerMethodAdapter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.SpringProperties;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
public class MessageEventListenerMethodProcessor
		implements SmartInitializingSingleton, ApplicationContextAware, BeanFactoryPostProcessor {

	/**
	 * 由spring.spel.ignore系统属性控制的布尔标志，指示 Spring 忽略 SpEL，即不初始化 SpEL 基础结构。 默认值为“假”。
	 */
	private static final boolean shouldIgnoreSpel = SpringProperties.getFlag("spring.spel.ignore");

	/**
	 * <p>
	 * 由于{@link MessageEventListenerMethodProcessor}实现{@link BeanFactoryPostProcessor}
	 * </p>
	 * <p>
	 * 所以{@link MessageEventListenerMethodProcessor}会被提前进行创建bean，然而依赖的{@link RedisMessageListenerContainer}bean
	 * </p>
	 * <p>
	 * 还没注入到容器中（配置类已加载解析但还未创建对应的Bean方法）,就会触发{@link MessageEventListenerAutoConfiguration}生效
	 * 去创建{@link RedisMessageListenerContainer}
	 * </p>
	 * <p>
	 * 但是由于{@link RedisMessageListenerContainer}依赖{@link RedisConnectionFactory},再去触发{@link RedisAutoConfiguration}
	 * </p>
	 * <p>
	 * 造成{@link RedisAutoConfiguration}在{@link MessageEventListenerAutoConfiguration}之后加载，Redis的自动配置内容未生效，因此报错
	 * </p>
	 *
	 * <p>
	 * 因此目前方案是在{@link MessageEventListenerMethodProcessor#afterSingletonsInstantiated()}内手动注入
	 * </p>
	 * <p>
	 * 还有很多其他方案，总之拿到BeanFactory手动注入
	 * </p>
	 *
	 */

	@Setter // RedisMessageListenerContainer container =
			// beanFactory.getBean(RedisMessageListenerContainer.class);
	private RedisMessageListenerContainer listenerContainer;

	@Setter // RedisTemplate template =
			// beanFactory.getBean("redisTemplate",RedisTemplate.class);
	private RedisSerializer redisSerializer;

	private ConfigurableApplicationContext applicationContext;

	private ConfigurableListableBeanFactory beanFactory;

	private List<MessageEventListenerFactory> messageEventListenerFactories;

	private final MessageEventExpressionEvaluator evaluator;

	private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(64));

	public MessageEventListenerMethodProcessor() {
		if (shouldIgnoreSpel) {
			this.evaluator = null;
		}
		else {
			this.evaluator = new MessageEventExpressionEvaluator();
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Assert.isTrue(applicationContext instanceof ConfigurableApplicationContext,
				"ApplicationContext does not implement ConfigurableApplicationContext");
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;

		Map<String, MessageEventListenerFactory> beans = beanFactory.getBeansOfType(MessageEventListenerFactory.class,
				false, false);
		List<MessageEventListenerFactory> factories = new ArrayList<>(beans.values());
		AnnotationAwareOrderComparator.sort(factories);
		this.messageEventListenerFactories = factories;
	}

	@Override
	public void afterSingletonsInstantiated() {
		ConfigurableListableBeanFactory beanFactory = this.beanFactory;
		Assert.state(this.beanFactory != null, "No ConfigurableListableBeanFactory set");
		// 手动注入RedisMessageListenerContainer
		try {
			RedisMessageListenerContainer container = beanFactory.getBean(RedisMessageListenerContainer.class);
			setListenerContainer(container);
			setRedisSerializer(RedisHelper.objectRedisTemplate().getValueSerializer());

		}
		catch (NoSuchBeanDefinitionException e) {
			//
			if (log.isDebugEnabled()) {
				log.error(
						"Could not find RedisMessageListenerContainer bean for MessageEventListenerMethodProcessor with name ",
						e);
			}
			throw e;
		}

		String[] beanNames = beanFactory.getBeanNamesForType(Object.class);
		for (String beanName : beanNames) {
			// 确认不是代理类
			if (!ScopedProxyUtils.isScopedTarget(beanName)) {
				Class<?> type = null;
				try {
					// 如果可能，确定指定 bean 的原始目标类，否则回退到常规getType查找
					type = AutoProxyUtils.determineTargetClass(beanFactory, beanName);
				}
				catch (Throwable ex) {
					// 一个无法解析的 bean 类型，可能来自一个惰性 bean - 忽略它。
					if (log.isDebugEnabled()) {
						log.debug("Could not resolve target class for bean with name '" + beanName + "'", ex);
					}
				}
				if (type != null) {
					if (ScopedObject.class.isAssignableFrom(type)) {
						try {
							Class<?> targetClass = AutoProxyUtils.determineTargetClass(beanFactory,
									ScopedProxyUtils.getTargetBeanName(beanName));
							if (targetClass != null) {
								type = targetClass;
							}
						}
						catch (Throwable ex) {
							// 无效的范围代理安排 - 忽略它。
							if (log.isDebugEnabled()) {
								log.debug("Could not resolve target bean for scoped proxy '" + beanName + "'", ex);
							}
						}
					}
					try {
						// 处理Bean
						processBean(beanName, type);
					}
					catch (Throwable ex) {
						throw new BeanInitializationException(
								"Failed to process @RedisListener " + "annotation on bean with name '" + beanName + "'",
								ex);
					}
				}
			}
		}
	}

	/**
	 * 处理符合的Bean
	 * @param beanName
	 * @param targetType
	 */
	private void processBean(final String beanName, final Class<?> targetType) {
		// 排除不解析的类型
		if (!this.nonAnnotatedClasses.contains(targetType)
				&& AnnotationUtils.isCandidateClass(targetType, RedisListener.class)
				&& !isSpringContainerClass(targetType)) {

			Map<Method, RedisListener> annotatedMethods = null;
			try {
				annotatedMethods = MethodIntrospector.selectMethods(targetType,
						(MethodIntrospector.MetadataLookup<RedisListener>) method -> AnnotatedElementUtils
								.findMergedAnnotation(method, RedisListener.class));
			}
			catch (Throwable ex) {
				// 方法签名中无法解析的类型，可能来自惰性 bean
				if (log.isDebugEnabled()) {
					log.debug("Could not resolve methods for bean with name '" + beanName + "'", ex);
				}
			}

			// 没有需要解析的Method
			if (CollectionUtils.isEmpty(annotatedMethods)) {
				this.nonAnnotatedClasses.add(targetType);
				if (log.isTraceEnabled()) {
					log.trace("No @RedisListener annotations found on bean class: " + targetType.getName());
				}
			}
			else {
				ConfigurableApplicationContext context = this.applicationContext;
				Assert.state(context != null, "No ApplicationContext set");
				List<MessageEventListenerFactory> factories = this.messageEventListenerFactories;
				Assert.state(factories != null, "MessageEventListenerFactory List not initialized");

				for (Method method : annotatedMethods.keySet()) {
					for (MessageEventListenerFactory factory : factories) {
						// 如果存在一个支持的工厂
						if (factory.supportsMethod(method)) {
							Method methodToUse = AopUtils.selectInvocableMethod(method, context.getType(beanName));
							MessageEventListener messageListener = factory.createMessageEventListener(beanName,
									targetType, methodToUse);
							if (messageListener instanceof MessageEventListenerMethodAdapter) {
								// 初始化实例
								((MessageEventListenerMethodAdapter) messageListener).init(applicationContext,
										evaluator, redisSerializer);
							}
							// 添加到容器
							listenerContainer.addMessageListener(messageListener, messageListener.topic());
							break;
						}
					}
				}
				if (log.isDebugEnabled()) {
					log.debug(annotatedMethods.size() + " @RedisListener methods processed on bean '" + beanName + "': "
							+ annotatedMethods);
				}
			}
		}
	}

	/**
	 * 确定给定的类是否是一个未注释为用户或测试Component的org.springframework bean 类......
	 * @return
	 */
	private static boolean isSpringContainerClass(Class<?> clazz) {
		return (clazz.getName().startsWith("org.springframework.")
				&& !AnnotatedElementUtils.isAnnotated(ClassUtils.getUserClass(clazz), Component.class));
	}

}
