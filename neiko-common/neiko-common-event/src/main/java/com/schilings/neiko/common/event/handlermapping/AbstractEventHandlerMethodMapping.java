package com.schilings.neiko.common.event.handlermapping;


import cn.hutool.core.collection.ConcurrentHashSet;
import com.schilings.neiko.common.event.handler.EvenHandlerMethodAdater;
import com.schilings.neiko.common.event.handler.EventHandler;
import com.schilings.neiko.common.event.handler.HandlerMethod;
import com.schilings.neiko.common.event.strategy.EvenMappingInfoGettingStrategy;
import com.schilings.neiko.common.event.strategy.EventHandlerSupportStrategy;
import com.schilings.neiko.common.event.strategy.HandlerMethodMappingNamingStrategy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.event.EventListenerMethodProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <pre>{@code
 *      
 * }
 * <p>抽象的 事件-方法类型处理器处理器 映射类</p>
 * <p>{@link EventListenerMethodProcessor}</p>
 * </pre>
 * @author Schilings
*/
@Slf4j
public abstract class AbstractEventHandlerMethodMapping<T extends EventMappingInfo>
        extends AbstractEventHandlerMapping implements InitializingBean {

    /**
     * Bean name prefix for target beans behind scoped proxies. Used to exclude those
     * targets from handler method detection, in favor of the corresponding proxies.
     * <p>We're not checking the autowire-candidate status here, which is how the
     * proxy target filtering problem is being handled at the autowiring level,
     * since autowire-candidate may have been turned to {@code false} for other
     * reasons, while still expecting the bean to be eligible for handler methods.
     * <p>Originally defined in {@link org.springframework.aop.scope.ScopedProxyUtils}
     * but duplicated here to avoid a hard dependency on the spring-aop module.
     */
    private static final String SCOPED_TARGET_NAME_PREFIX = "scopedTarget.";

    /**
     * 默认命名策略是 事件类的全限定名
     */
    private final HandlerMethodMappingNamingStrategy DEFAULT_NAMING_STRATEGY = o -> {
        return ClassUtils.getUserClass(o).getName();
    };

    /**
     * 默认匹配策略是 映射唯一Name是否与命名策略生成的相同
     */
    private final EventHandlerSupportStrategy<T> DEFAULT_SUPPORT_STRATEGY = (event, mapping) -> {
        return mapping.getUniqueName() == DEFAULT_NAMING_STRATEGY.getName(event);
    };

    
    private final EvenMappingInfoGettingStrategy DEFAULT_GETTING_STRATEGY = (registry, event) -> {
        return registry.getMappingInfoSet();
                
    };

    /**
     * 是否要扫描父容器，一般为false不用
     */
    private boolean detectHandlerMethodsInAncestorContexts = false;

    public void setDetectHandlerMethodsInAncestorContexts(boolean detectHandlerMethodsInAncestorContexts) {
        this.detectHandlerMethodsInAncestorContexts = detectHandlerMethodsInAncestorContexts;
    }

    /**
     * 映射注册表
     */
    private final EventMappingRegistry mappingRegistry = new EventMappingRegistry();


    /**
     * 映射唯一命名策略
     */
    private HandlerMethodMappingNamingStrategy namingStrategy = DEFAULT_NAMING_STRATEGY;

    /**
     * 判断事件与处理器匹配策略
     */
    private EventHandlerSupportStrategy supportStrategy = DEFAULT_SUPPORT_STRATEGY;

    /**
     * 从注册表取出处理器策略
     */
    private EvenMappingInfoGettingStrategy gettingStrategy = DEFAULT_GETTING_STRATEGY;
    

    public void setGettingStrategy(EvenMappingInfoGettingStrategy gettingStrategy) {
        this.gettingStrategy = gettingStrategy;
    }

    public void setNamingStrategy(HandlerMethodMappingNamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
    }

    public void setSupportStrategy(EventHandlerSupportStrategy supportStrategy) {
        this.supportStrategy = supportStrategy;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initHandlerMethod();
    }

    /**
     * 初始化方法类型的事件处理器
     */
    protected void initHandlerMethod() {
        for (String beanName : getCandidateBeanNames()) {
            //
            if (!beanName.startsWith(SCOPED_TARGET_NAME_PREFIX)) {
                processCandidateBean(beanName);
            }
        }
        //
        handlerMethodsInitialized(getHandlerMethods());
    }

    /**
     * 确定应用程序上下文中候选 bean 的名称。
     * @return
     */
    protected String[] getCandidateBeanNames() {
        return (this.detectHandlerMethodsInAncestorContexts ?
                BeanFactoryUtils.beanNamesForTypeIncludingAncestors(obtainApplicationContext(), Object.class) :
                obtainApplicationContext().getBeanNamesForType(Object.class));
    }

    /**
     * 确定指定候选 bean 的类型，如果标识为处理程序类型，则调用detectHandlerMethods 
     * @param beanName
     */
    protected void processCandidateBean(String beanName) {
        Class<?> beanType = null;
        try {
            beanType = obtainApplicationContext().getType(beanName);
        }
        catch (Throwable ex) {
            //如果这个Bean获取不到
            if (logger.isTraceEnabled()) {
                logger.trace("Could not resolve type for bean '" + beanName + "'", ex);
            }
        }
        if (beanType != null && isHandler(beanType)) {
            //解析这个Bean里面的事件监听方法
            detectHandlerMethods(beanName);
        }
    }

    /**
     * 在指定的处理程序 bean 中查找处理程序方法。
     * @param handler
     */
    protected void detectHandlerMethods(Object handler) {
        //先判断传入的是bean的名字还是bean的类型
        Class<?> handlerType = (handler instanceof String ?
                obtainApplicationContext().getType((String) handler) : handler.getClass());
        
        if (handlerType != null) {
            //返回给定类的用户定义类：通常只是给定类，但如果是 CGLIB 生成的子类，则返回原始类。
            Class<?> userType = ClassUtils.getUserClass(handlerType);
            //遍历这个类中的方法，进行映射绑定
            Set<Method> methods = MethodIntrospector.selectMethods(userType,
                    (ReflectionUtils.MethodFilter) method->{
                        return isHandlerMethod(method);
                    });
            Map<Method, T> methodTMap = new HashMap<>();
            methods.forEach(m->{
                try {
                    methodTMap.put(m, getMappingForMethod(m, userType));
                }
                catch (Throwable ex) {
                    throw new IllegalStateException("Invalid mapping on handler class [" +
                            userType.getName() + "]: " + m, ex);
                }
            });
//            Map<Method, T> methods = MethodIntrospector.selectMethods(userType,
//                    (MethodIntrospector.MetadataLookup<T>) method -> {
//                        try {
//                            return getMappingForMethod(method, userType);
//                        }
//                        catch (Throwable ex) {
//                            throw new IllegalStateException("Invalid mapping on handler class [" +
//                                    userType.getName() + "]: " + method, ex);
//                        }
//                    });
            if (logger.isTraceEnabled()) {
                logger.trace(formatMappings(userType, methodTMap));
            }
            else if (logger.isDebugEnabled()) {
                logger.debug(formatMappings(userType, methodTMap));
            }
            
            //遍历方法，绑定目标类型，返回目标类型上对应的可调用方法
            methodTMap.forEach((method, mapping) -> {
                Method invocableMethod = AopUtils.selectInvocableMethod(method, userType);
                registerHandlerMethod(handler, invocableMethod, mapping);
            });
        }
        
    }

    /**
     * 注册给定的映射。
     * 初始化完成后，可以在运行时调用此方法。
     * @param mapping
     * @param handler
     * @param method
     */
    public void registerMapping(T mapping, Object handler, Method method) {
        if (logger.isTraceEnabled()) {
            logger.trace("Register \"" + mapping + "\" to " + method.toGenericString());
        }
        this.mappingRegistry.register(mapping, handler, method);
    }


    /**
     * 取消注册给定的映射。
     * 初始化完成后，可以在运行时调用此方法。
     * @param mapping
     */
    public void unregisterMapping(T mapping) {
        if (logger.isTraceEnabled()) {
            logger.trace("Unregister mapping \"" + mapping + "\"");
        }
        this.mappingRegistry.unregister(mapping);
    }

    /**
     * 模板方法，判断该方法是否匹配
     * @param method
     * @return
     */
    protected abstract boolean isHandlerMethod(Method method);

    /**
     * 注册处理程序方法及其唯一映射。在启动时为每个检测到的处理程序方法调用
     * @param handler
     * @param method
     * @param mapping
     */
    protected void registerHandlerMethod(Object handler, Method method, T mapping){
        this.mappingRegistry.register(mapping, handler, method);
    }

    /**
     * 模板方法，对指定方法定制映射
     * @param method
     * @param userType
     * @return
     */
    protected abstract T getMappingForMethod(Method method, Class<?> userType);


    /**
     * 源自Spring
     * @param handlerMethods
     */
    protected void handlerMethodsInitialized(Map<T, HandlerMethod> handlerMethods) {
        // Total includes detected mappings + explicit registrations via registerMapping
        int total = handlerMethods.size();
        if ((logger.isTraceEnabled() && total == 0) || (logger.isDebugEnabled() && total > 0) ) {
            
        }
    }
    
    

    /**
     * 返回具有所有映射和 HandlerMethod 的（只读）映射
     * @return
     */
    public Map<T, HandlerMethod> getHandlerMethods() {
        this.mappingRegistry.acquireReadLock();
        try {
            return Collections.unmodifiableMap(
                    this.mappingRegistry.getRegistrations().entrySet().stream()
                            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().handlerMethod)));
        }
        finally {
            this.mappingRegistry.releaseReadLock();
        }
    }
    
    /**
     * 源自Spring MVC
     * @param userType
     * @param methods
     * @return
     */
    private String formatMappings(Class<?> userType, Map<Method, T> methods) {
        String packageName = ClassUtils.getPackageName(userType);
        String formattedType = (StringUtils.hasText(packageName) ?
                Arrays.stream(packageName.split("\\."))
                        .map(packageSegment -> packageSegment.substring(0, 1))
                        .collect(Collectors.joining(".", "", "." + userType.getSimpleName())) :
                userType.getSimpleName());
        Function<Method, String> methodFormatter = method -> Arrays.stream(method.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(",", "(", ")"));
        return methods.entrySet().stream()
                .map(e -> {
                    Method method = e.getKey();
                    return e.getValue() + ": " + method.getName() + methodFormatter.apply(method);
                })
                .collect(Collectors.joining("\n\t", "\n\t" + formattedType + ":" + "\n\t", ""));
    }


    /**
     * 实际获得Handler
     * @param event
     * @return
     */
    @Override
    protected List<EventHandler> getHandlerInternal(Object event) {
        this.mappingRegistry.acquireReadLock();
        try {
            List<EventHandler> eventHandlers = lookupEventHandler(event, this.mappingRegistry);
            if (eventHandlers == null || eventHandlers.size() <= 0) {
                return Collections.emptyList();
            }
            return eventHandlers;
        }
        finally {
            this.mappingRegistry.releaseReadLock();
        }
    }


    /**
     * 寻找返回匹配的事件处理器
     * @param event
     * @param registry
     * @return
     */
    protected List<EventHandler> lookupEventHandler(Object event, EventMappingRegistry registry) {
        Set<T> mappingSet = new HashSet<T>(gettingStrategy.get(registry, event));
        Map<T, EventMappingRegistration<T>> registrations = this.mappingRegistry.getRegistrations();
        //匹配的事件处理器
        List<EventHandler> matchedHandlers = new LinkedList<>();
        for (T t : mappingSet) {
            if (supportStrategy.support(event, t)) {
                EventMappingRegistration<T> registration =registrations.get(t);
                matchedHandlers.add(registration.getEventHandler());
            }
        }
        if (matchedHandlers == null || matchedHandlers.isEmpty()) {
            return Collections.emptyList();
        }
        return new LinkedList<>(matchedHandlers);
    }
    
    /**
     * <pre>{@code
     *
     * }
     * <p>事件映射关系 注册器</p>
     * </pre>
     * @author Schilings
     */

    public class EventMappingRegistry{

        /**
         * 持有所以映射关系
         */
        
        private final Map<T, EventMappingRegistration<T>> registry = new HashMap<>();
        /**
         * 通过事件名寻找
         */
        @Getter
        private final Map<String, Set<EventHandler>> nameLookup = new ConcurrentHashMap<>();
        @Getter
        private final Set<EventHandler> allHandlers = new ConcurrentHashSet<>();
        @Getter
        private final Set<HandlerMethod> allMethods = new ConcurrentHashSet<>();

        /**
         * 使用一个读写锁已经映射关系管理
         */
        private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();


        public void acquireReadLock() {
            this.readWriteLock.readLock().lock();
        }


        public void releaseReadLock() {
            this.readWriteLock.readLock().unlock();
        }

        public void acquireWriteLock() {
            this.readWriteLock.readLock().lock();
        }

        public void releaseWriteLock() {
            this.readWriteLock.readLock().unlock();
        }


        /**
         * 返回所有注册信息
         * @return
         */
        public Map<T, EventMappingRegistration<T>> getRegistrations() {
            return this.registry;
        }


        /**
         * 返回所有注册信息
         */
        public Set<EventMappingRegistration<T>> getRegistrationsSet() {
            return new HashSet<>(this.registry.values());
        }

        /**
         * 返回所有映射信息
         * @return
         */
        public Set<T> getMappingInfoSet() {
            return new HashSet<>(this.registry.keySet());
        }
        
        
        /**
         * 注册映射关系
         * @param mapping
         * @param handler
         * @param method
         */
        public  void register(T mapping, Object handler, Method method) {
            acquireWriteLock();
            try {
                //实例化处理方法
                HandlerMethod handlerMethod = createHandlerMethod(handler, method);
                validateMethodMapping(handlerMethod, mapping);
                //放入注册表中
                EvenHandlerMethodAdater methodAdater = new EvenHandlerMethodAdater(handlerMethod);
                EventMappingRegistration<T> registration = new EventMappingRegistration<>(mapping, handlerMethod, mapping.getUniqueName(), methodAdater);
                this.registry.put(mapping, registration);
                Set<EventHandler> handlers = this.nameLookup.get(registration.getMappingsName());
                if (handlers == null) {
                    handlers = new LinkedHashSet<>();
                    this.nameLookup.put(registration.getMappingsName(), handlers);
                }
                handlers.add(methodAdater);
                this.allHandlers.add(methodAdater);
                this.allMethods.add(handlerMethod);
                
            }finally {
                releaseWriteLock();
            }
        }

        /**
         * 取消映射关系
         * @param mapping
         */
        public void unregister(T mapping) {
            acquireWriteLock();
            try {
                EventMappingRegistration<T> registration = this.registry.remove(mapping);
                Set<EventHandler> handlers = this.nameLookup.get(registration.getMappingsName());
                if (handlers != null && handlers.size() > 0) {
                    EvenHandlerMethodAdater methodAdater = new EvenHandlerMethodAdater(registration.getHandlerMethod());
                    handlers.remove(methodAdater);
                    this.allHandlers.remove(methodAdater);
                    this.allMethods.remove(registration.getHandlerMethod());
                }
                if (registration == null) {
                    return;
                }
            }finally {
                releaseWriteLock();
            }
            
        }

        /**
         * 根据映射唯一名获得事件处理器集合
         * @param mappingName
         * @return
         */
        public Set<EventHandler> getEventHandlersByMappingName(String mappingName) {
            Set<EventHandler> handlers = this.nameLookup.get(mappingName);
            return (handlers != null ? handlers : Collections.emptySet());
        }


        /**
         * 源自Spring
         * @param handler
         * @param method
         * @return
         */
        protected HandlerMethod createHandlerMethod(Object handler, Method method) {
            if (handler instanceof String) {
                return new HandlerMethod((String) handler,
                        obtainApplicationContext().getAutowireCapableBeanFactory(),
                        obtainApplicationContext(),
                        method);
            }
            return new HandlerMethod(handler, method);
        }


        /**
         * 源自Spring
         * @param handlerMethod
         * @param mapping
         */
        private void validateMethodMapping(HandlerMethod handlerMethod, T mapping) {
            EventMappingRegistration<T> registration = this.registry.get(mapping);
            HandlerMethod existingHandlerMethod = (registration != null ? registration.getHandlerMethod() : null);
            if (existingHandlerMethod != null && !existingHandlerMethod.equals(handlerMethod)) {
                throw new IllegalStateException(
                        "Ambiguous mapping. Cannot map '" + handlerMethod.getBean() + "' method \n" +
                                handlerMethod + "\nto " + mapping + ": There is already '" +
                                existingHandlerMethod.getBean() + "' bean method\n" + existingHandlerMethod + " mapped.");
            }
        }
        
    }

    /**
     * 注册表
     * @param <T>
     */
    public static class EventMappingRegistration<T>{
        /**
         * 映射信息封装类
         */
        private final T mappingInfo;

        private final String mappingName;
        
        /**
         * SpringAop的执行方法类
         */
        private final HandlerMethod handlerMethod;

        /**
         * 事件处理类
         *
         * @return
         */
        private final EventHandler eventHandler;

        

        public T getMappingInfo() {
            return mappingInfo;
        }

        public HandlerMethod getHandlerMethod() {
            return handlerMethod;
        }

        public String getMappingsName() {
            return mappingName;
        }


        public EventHandler getEventHandler() {
            return eventHandler;
        }

        EventMappingRegistration(T mappingInfo, HandlerMethod handlerMethod, String mappingName, EventHandler eventHandler) {
            Assert.notNull(mappingInfo, "Mapping must not be null");
            Assert.notNull(handlerMethod, "HandlerMethod must not be null");
            Assert.notNull(handlerMethod, "EventHandler must not be null");
            Assert.notNull(handlerMethod, "MappingName must not be null");
            this.mappingInfo = mappingInfo;
            this.handlerMethod = handlerMethod;
            this.mappingName = mappingName;
            this.eventHandler = eventHandler;
        }
    }


    /**
     * 围绕匹配的 HandlerMethod 及其映射的瘦包装器
     */
    private class Match {

        private final T mapping;

        private final EventMappingRegistration<T> registration;

        public Match(T mapping, EventMappingRegistration<T> registration) {
            this.mapping = mapping;
            this.registration = registration;
        }

        public HandlerMethod getHandlerMethod() {
            return this.registration.getHandlerMethod();
        }

        @Override
        public String toString() {
            return this.mapping.toString();
        }
    }

}
