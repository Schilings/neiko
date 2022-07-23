package com.schilings.neiko.common.cache.advisor;


import com.schilings.neiko.common.cache.components.*;
import com.schilings.neiko.common.cache.operation.*;
import com.schilings.neiko.common.cache.parser.NeikoCacheAnnotationParser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.schilings.neiko.common.cache.components.ExpressionCacheEvaluator.NO_RESULT;

/**
 * <pre>
 * <p>AspectJ切面,对缓存操作的核心支持类</p>
 * </pre>
 * @author Schilings
*/
@Slf4j
@Setter
@Getter
public abstract class NeikoCacheAspectSupport implements BeanFactoryAware, InitializingBean, SmartInitializingSingleton {

    /**
     * 初始化状态
     */
    private boolean initialized = false;


    /**
     * 缓存条件解析器
     */
    @Getter
    @Setter
    private final ExpressionCacheEvaluator evaluator = new ExpressionCacheEvaluator();
    
    
    /**
     * 缓存管理
     */
    @Getter
    @Setter
    private CacheManager cacheManager;

    
    /**
     * 错误处理器
     */
    @Getter
    @Setter
    private CacheErrorHandler errorHandler;

    
    /**
     * 缓存键生成器
     */
    @Getter
    @Setter
    private KeyGenerator keyGenerator;

    
    /**
     * 容器工厂
     */
    @Nullable
    private BeanFactory beanFactory;


    /**
     * 缓存注解解析器
     */
    @Getter
    @Setter
    private NeikoCacheAnnotationParser annotationParser;
    

    
    /**
     * 使用给定的错误处理程序、密钥生成器和缓存解析器/管理器
     * @param cacheManager
     * @param keyGenerator
     * @param errorHandler
     */
    public void configure(CacheManager cacheManager, KeyGenerator keyGenerator, CacheErrorHandler errorHandler) {
        this.cacheManager = cacheManager;
        this.keyGenerator = keyGenerator;
        this.errorHandler = errorHandler;
    }



    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }


    /**
     * 初始化
     */
    @Override
    public void afterSingletonsInstantiated() {
        //去容器中取出缓存管理器
        Assert.state(this.beanFactory != null, "CacheEvaluator or BeanFactory must be set on cache aspect");
        try {
            //取出缓存管理器！！
            setCacheManager(this.beanFactory.getBean(CacheManager.class));
        }
        catch (NoUniqueBeanDefinitionException ex) {
            throw new IllegalStateException("No CacheEvaluator specified, and no unique bean of type " +
                    "CacheManager found. Mark one as primary or declare a specific CacheManager to use.", ex);
        }
        catch (NoSuchBeanDefinitionException ex) {
            throw new IllegalStateException("No CacheEvaluator specified, and no bean of type CacheManager found. " +
                    "Register a CacheManager bean or remove the @EnableCaching annotation from your configuration.", ex);
        }
        this.initialized = true;
    }


    /**
     * 执行缓存操作的方法！！！
     * @param invoker
     * @param target
     * @param method
     * @param args
     * @return
     */
    protected Object excute(CacheOperationInvoker invoker, Object target, Method method, Object[] args) {
        //确保完成初始化
        if (this.initialized) {
            //确定给定 bean 实例的最终目标类，不仅遍历顶级代理，还遍历任意数量的嵌套代理 — 尽可能长且没有副作用，即仅针对单例目标。
            Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
            //拿出缓存操作（如果map缓存有就去拿，没有就解析后拿）
            if (annotationParser != null) {
                Collection<CacheOperation> operations = annotationParser.getCacheOperations(method, targetClass);
                //如果缓存操作不为空，那么进行缓存处理
                if (!CollectionUtils.isEmpty(operations)) {
                    //只取一个
                    CacheOperation operation = operations.iterator().next();

                    //=============这里注意，方法已经运行！，不要重复执行运行操作；=====================
                    Object result = invoker.invoke();
                    //=================这里注意，方法已经运行！，不要重复执行运行操作；=====================
                    
                    CacheOperationContext operationContext = new CacheOperationContext(operation, method, args, target, targetClass, result, beanFactory);
                    
                    //满足的是否condition条件
                    if (isConditionPassing(operation.getCondition(), operationContext)) {
                        return excuteCached(invoker, operationContext);
                    }
                }
            }
        }
        //如果没有完成初始化，直接执行方法返回
        return invoker.invoke();
    }


    /**
     * 使用缓存条件解析器判断缓存条件是否满足
     * @return
     */
    protected boolean isConditionPassing(String conditon,CacheOperationContext operationContext)  {
        boolean conditionPassing = false;
        //如果condition不为空，则进行匹配判断
        if (StringUtils.hasText(conditon)) {
            EvaluationContext evaluationContext = createEvaluationContext(operationContext);
            conditionPassing = evaluator.condition(
                    conditon, 
                    new AnnotatedElementKey(operationContext.method,operationContext. targetClass), 
                    evaluationContext);
        } else {
            //条件为空，默认都过
            conditionPassing = true;
        }
        return conditionPassing;
    }


    /**
     * 拒绝缓存条件是否满足
     * @param unless
     * @param operationContext
     * @return
     */
    protected boolean isUnlessPassing(String unless,CacheOperationContext operationContext)  {
        boolean unlessPassing = true;
        //条件为空，进行匹配判断
        if (StringUtils.hasText(unless)) {
            EvaluationContext evaluationContext = createEvaluationContext(operationContext);
            unlessPassing = evaluator.unless(
                    unless,
                    new AnnotatedElementKey(operationContext.method, operationContext.targetClass), 
                    evaluationContext);
        } else {
            //条件为空，默认都不拒绝缓存，即都给过
            unlessPassing = false;
        }
        return unlessPassing;
    }

    /**
     * 使用缓存条件解析器生成缓存Key
     * @return
     */
    protected String generateKey(CacheOperationContext operationContext) {//;
        //如果key不为空，优先使用key
        if (StringUtils.hasText(operationContext.operation.getKey())) {
            EvaluationContext evaluationContext = createEvaluationContext(operationContext);
            return evaluator.key(
                    operationContext.operation.getKey(),
                    new AnnotatedElementKey(operationContext.method, operationContext.targetClass), 
                    evaluationContext);
        }
        //如果key为空，那么使用KeyGenerator
        if (keyGenerator == null) {
            log.trace("Key Generator do not exist.");
            throw new RuntimeException("Key Generator do not exist."); 
        }
        return keyGenerator.generate(operationContext.target, operationContext.method, operationContext.args);
    }

    /**
     * 生成Spel表达式上下文
     * @return
     */
    private EvaluationContext createEvaluationContext(CacheOperationContext context) {
        return evaluator.createEvaluationContext(
                context.method, 
                context.args, 
                context.target, 
                context.result, 
                context.beanFactory);
    }
    

    /**
     * 执行缓存
     * @param invoker
     * @return
     */
    protected Object excuteCached(CacheOperationInvoker invoker, CacheOperationContext operationContext) {
        CacheableOperation cacheable = null;
        CachePutOperation cachePut = null;
        CacheEvictOperation cacheEvict = null;
        //处理@NeikoCacheable
        if (operationContext.operation instanceof CacheableOperation) {
            cacheable = (CacheableOperation) operationContext.operation;
            operationContext.setOperation(cacheable);
            return excuteCacheable(invoker, cacheable,operationContext);
        }
        //处理@NeikoCachePut
        if (operationContext.operation instanceof CachePutOperation) {
            cachePut = (CachePutOperation) operationContext.operation;
            operationContext.setOperation(cachePut);
            return excuteCachePut(invoker, cachePut,operationContext);
        }
        //处理@NeikoCacheEvict
        if (operationContext.operation instanceof CacheEvictOperation) {
            cacheEvict = (CacheEvictOperation) operationContext.operation;
            operationContext.setOperation(cacheEvict);
            return excuteCacheEvict(invoker, cacheEvict,operationContext);
        }
        //如果没有注解
        return operationContext.getResult();
    }
    
    //不同注解方式的不同缓存策略===========================
    
    
    /**
     * NeikoCacheable方式的执行缓存
     * @param invoker
     * @param operation
     * @return
     */
    protected Object excuteCacheable(CacheOperationInvoker invoker, CacheableOperation operation, CacheOperationContext operationContext) {
        
        //满足拒绝缓存的条件时
        if (isUnlessPassing(operation.getUnless(),operationContext)) {
            return operationContext.getResult();
        }
        
        //缓存执行
        String key = generateKey(operationContext);
        String repository = operation.getCacheRepository();
        long ttl = operation.getTtl();
        TimeUnit unit = operation.getUnit();
        //尝试取出缓存
        Object cacheResult = get(repository, key, operation.isSync());
        if (cacheResult == null) {
            Object result = operationContext.getResult();
            //如果运行结果不为null，则放入缓存中
            if (result != null) {
                put(repository, key, result, ttl, unit, ttl >= 0, operation.isSync());

            }
            return result;
        } else {
            return cacheResult;
        }
    }


    /**
     * NeikoCacheEvict方式的执行缓存
     * @param invoker
     * @param cacheEvict
     * @param operationContext
     * @return
     */
    private Object excuteCacheEvict(CacheOperationInvoker invoker, CacheEvictOperation cacheEvict, CacheOperationContext operationContext) {
        return operationContext.getResult();
    }

    /**
     * NeikoCachePut方式的执行缓存
     * @param invoker
     * @param cachePut
     * @param operationContext
     * @return
     */
    private Object excuteCachePut(CacheOperationInvoker invoker, CachePutOperation cachePut, CacheOperationContext operationContext) {
        return operationContext.getResult();
    }
    
    
    //缓存仓库相关的操作=========================


    private Object get(String repository, String key, boolean sync) {
        return sync ? doSyncGet(repository, key) : doGet(repository, key);
    }
    
    protected Object doGet(String repository,String key) {
        try {
            return cacheManager.get(repository,key);
        } catch (RuntimeException exception) {
            getErrorHandler().handleCacheGetError(exception, key);
            //如果处理了异常，则返回缓存未找到
            return null; 
        }
    }

    protected Object doSyncGet(String repository,String key) {
        try {
            return cacheManager.syncGet(repository,key);
        } catch (RuntimeException exception) {
            getErrorHandler().handleCacheGetError(exception, key);
            //如果处理了异常，则返回缓存未找到
            return null;
        }
    }


    private void put(String repository, String key, Object result, long ttl, TimeUnit unit, boolean flag, boolean sync) {
        if (sync) {
            if (flag) {
                doSyncPut(repository, key, result, ttl, unit);
            } else {
                doSyncPut(repository, key, result);
            }
        } else {
            if (flag) {
                doPut(repository, key, result, ttl, unit);
            } else {
                doPut(repository, key, result);
            }
        }
    }

    protected void doPut(String repository,String key, Object value) {
        try {
            cacheManager.put(repository,key, value);
        }catch (RuntimeException exception) {
            getErrorHandler().handleCachePutError(exception, key, value);
        }
    }

    protected void doPut(String repository,String key, Object value, long ttl, TimeUnit unit) {
        try {
            cacheManager.put(repository,key, value, ttl, unit);
        }catch (RuntimeException exception) {
            getErrorHandler().handleCachePutError(exception, key, value);
        }
    }

    protected void doSyncPut(String repository,String key, Object value) {
        try {
            cacheManager.syncPut(repository,key, value);
        }catch (RuntimeException exception) {
            getErrorHandler().handleCachePutError(exception, key, value);
        }
    }
    
    protected void doSyncPut(String repository,String key, Object value, long ttl, TimeUnit unit) {
        try {
            cacheManager.syncPut(repository,key, value, ttl, unit);
        }catch (RuntimeException exception) {
            getErrorHandler().handleCachePutError(exception, key, value);
        }
    }

    protected void doEvict(String repository,String  key, boolean immediate) {
        try {
            if (immediate) {
                cacheManager.evictIfPresent(repository,key);
            } else {
                cacheManager.evict(repository,key);
                
            } 
        }catch (RuntimeException exception) {
            getErrorHandler().handleCacheEvictError(exception, key);
            
        }
    }

    
    /**
     * <pre>
     * <p>缓存操作上下文：对应的类和方法的信息</p>
     * </pre>
     * @author Schilings
    */
    @Data
    @AllArgsConstructor
    public class CacheOperationContext {
        private CacheOperation operation;
        private Method method;
        private Object[] args; 
        private Object target;
        private Class<?> targetClass;
        private Object result;
        private BeanFactory beanFactory;
    }
    
    
}
