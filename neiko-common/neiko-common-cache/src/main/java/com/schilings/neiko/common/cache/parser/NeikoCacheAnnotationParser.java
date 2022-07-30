package com.schilings.neiko.common.cache.parser;

import com.schilings.neiko.common.cache.advisor.NeikoCacheAspectSupport;
import com.schilings.neiko.common.cache.annotation.NeikoCacheConfig;
import com.schilings.neiko.common.cache.annotation.NeikoCacheEvict;
import com.schilings.neiko.common.cache.annotation.NeikoCachePut;
import com.schilings.neiko.common.cache.annotation.NeikoCacheable;
import com.schilings.neiko.common.cache.operation.CacheEvictOperation;
import com.schilings.neiko.common.cache.operation.CacheOperation;
import com.schilings.neiko.common.cache.operation.CachePutOperation;
import com.schilings.neiko.common.cache.operation.CacheableOperation;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * <p>对给定类和方法解析出要缓存的方法和注释信息</p>
 * <p>为{@link NeikoCacheAspectSupport}支持解析的组件</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
public class NeikoCacheAnnotationParser implements CacheAnnotationParser, Serializable {

	/**
	 * 缓存中保存的规范值表示没有找到此方法的缓存属性，我们不需要再次查看
	 */
	private static final Collection<CacheOperation> NULL_CACHING_ATTRIBUTE = Collections.emptyList();

	/**
	 * CacheOperations 的缓存，由特定目标类上的方法键入。 缓存将在序列化后重新创建——前提是 Serializable。
	 */
	private final Map<Object, Collection<CacheOperation>> attributeCache = new ConcurrentHashMap<>(1024);

	private static final Set<Class<? extends Annotation>> CACHE_OPERATION_ANNOTATIONS = new LinkedHashSet<>(8);

	/**
	 * 是否只允许public缓存
	 */
	@Setter
	private boolean publicMethodsOnly = false;

	public boolean allowPublicMethodsOnly() {
		return publicMethodsOnly;
	}

	/**
	 * 支持的注解
	 */
	static {
		CACHE_OPERATION_ANNOTATIONS.add(NeikoCacheable.class);
		CACHE_OPERATION_ANNOTATIONS.add(NeikoCachePut.class);
		CACHE_OPERATION_ANNOTATIONS.add(NeikoCacheEvict.class);
	}

	/**
	 * 确定给定类是否解析类方法中的缓存操作的候选对象。 如果此方法返回false ，则不会遍历给定类上的方法以进行getCacheOperations内省。
	 * 因此，返回false是对不受影响的类的优化，而true仅表示该类需要针对给定类上的每个方法单独进行完全自省。
	 * @param targetClass
	 * @return
	 */
	public boolean isCandidateClass(Class<?> targetClass) {
		return AnnotationUtils.isCandidateClass(targetClass, CACHE_OPERATION_ANNOTATIONS);
	}

	/**
	 * 确定此方法调用的缓存属性。 如果没有找到方法属性，则默认为类的缓存属性 找到并放到缓存Map中
	 * @param method
	 * @param targetClass
	 * @return
	 */
	@Override
	public Collection<CacheOperation> getCacheOperations(Method method, Class<?> targetClass) {
		if (method.getDeclaringClass() == Object.class) {
			return null;
		}
		// 如果在Map缓存中取不到，那就去解析
		Object key = getCacheKey(method, targetClass);
		Collection<CacheOperation> cached = this.attributeCache.get(key);

		if (cached != null) {
			return (cached != NULL_CACHING_ATTRIBUTE ? cached : null);
		}
		else {
			// 去解析
			Collection<CacheOperation> cacheOps = computeCacheOperations(method, targetClass);
			if (cacheOps != null) {
				if (log.isTraceEnabled()) {
					log.trace("Adding cacheable method '" + method.getName() + "' with attribute: " + cacheOps);
				}
				this.attributeCache.put(key, cacheOps);
			}
			else {
				// 如果解析不到任何缓存操作，那么传入空值标记
				this.attributeCache.put(key, NULL_CACHING_ATTRIBUTE);
			}
			return cacheOps;
		}
	}

	/**
	 * 创建缓存操作在缓存Map里的键
	 * @param method
	 * @param taretClass
	 * @return
	 */
	protected Object getCacheKey(Method method, Class<?> taretClass) {
		return new MethodClassKey(method, taretClass);
	}

	/**
	 * 解析获取缓存操作
	 * @param method
	 * @param targetClass
	 * @return
	 */
	protected Collection<CacheOperation> computeCacheOperations(Method method, Class<?> targetClass) {
		// Don't allow non-public methods, as configured.
		if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
			return null;
		}

		// 该方法可能在接口上，但我们需要来自目标类的属性。如果目标类为空，则方法将保持不变
		// 给定一个可能来自接口的方法，以及当前 AOP 调用中使用的目标类，如果有，则找到对应的目标方法。
		Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);

		// 1.尝试解析的是 目标类中的方法(可能来自接口的原始方法)
		Collection<CacheOperation> opDef = findCacheOperations(specificMethod);
		if (opDef != null) {
			return opDef;
		}

		// 2.尝试解析的是 目标类
		opDef = findCacheOperations(specificMethod.getDeclaringClass());
		if (opDef != null && ClassUtils.isUserLevelMethod(method)) {
			return opDef;
		}

		// 如果确实是来自接口的原始方法
		if (specificMethod != method) {
			// 3.则尝试解析原来的自己的方法
			opDef = findCacheOperations(method);
			if (opDef != null) {
				return opDef;
			}
			// 4.最后一个备用是原始方法的类。
			opDef = findCacheOperations(method.getDeclaringClass());
			if (opDef != null && ClassUtils.isUserLevelMethod(method)) {
				return opDef;
			}
		}
		return null;
	}

	/**
	 * 确定给定的方法解析得到的缓存操作---find层。
	 * @param method
	 * @return
	 */
	protected Collection<CacheOperation> findCacheOperations(Method method) {
		return parseCacheAnnotations(method);
	}

	protected Collection<CacheOperation> findCacheOperations(Class<?> clz) {
		return parseCacheAnnotations(clz);
	}

	/**
	 * 确定给定的方法解析得到的缓存操作---detemine层。
	 * @return
	 */
	Collection<CacheOperation> determineCacheOperations(Method method) {
		Collection<CacheOperation> ops = null;
		Collection<CacheOperation> annOps = parseCacheAnnotations(method);
		if (annOps != null) {
			if (ops == null) {
				ops = annOps;
			}
			else {
				// 兼顾多个parser的请款，之后可能扩展多个parser
				// 不然直接return parseCacheAnnotations(method);
				Collection<CacheOperation> combined = new ArrayList<>(ops.size() + annOps.size());
				combined.addAll(ops);
				combined.addAll(annOps);
				ops = combined;
			}
		}
		return ops;
	}

	Collection<CacheOperation> determineCacheOperations(Class clz) {
		Collection<CacheOperation> ops = null;
		Collection<CacheOperation> annOps = parseCacheAnnotations(clz);
		if (annOps != null) {
			if (ops == null) {
				ops = annOps;
			}
			else {
				// 兼顾多个parser的请款，之后可能扩展多个parser
				// 不然直接return parseCacheAnnotations(method);
				Collection<CacheOperation> combined = new ArrayList<>(ops.size() + annOps.size());
				combined.addAll(ops);
				combined.addAll(annOps);
				ops = combined;
			}
		}
		return ops;
	}

	/**
	 * 确定给定的方法解析得到的缓存操作---parse层。
	 * @param type
	 * @return
	 */

	public Collection<CacheOperation> parseCacheAnnotations(Class<?> type) {
		DefaultCacheConfig defaultConfig = new DefaultCacheConfig(type);
		return parseCacheAnnotations(defaultConfig, type);
	}

	public Collection<CacheOperation> parseCacheAnnotations(Method method) {
		DefaultCacheConfig defaultConfig = new DefaultCacheConfig(method.getDeclaringClass());
		return parseCacheAnnotations(defaultConfig, method);
	}

	/**
	 * 解析
	 * @return
	 */
	@Nullable
	private Collection<CacheOperation> parseCacheAnnotations(DefaultCacheConfig cachingConfig, AnnotatedElement ae) {
		Collection<CacheOperation> ops = parseCacheAnnotations(cachingConfig, ae, false);
		if (ops != null && ops.size() > 1) {
			// 发现多个操作 -> 本地声明覆盖接口声明的...
			// 如果发现有多个注解声明，可能来自父类或者接口，则使用本地声明覆盖接口声明
			Collection<CacheOperation> localOps = parseCacheAnnotations(cachingConfig, ae, true);
			if (localOps != null) {
				return localOps;
			}
		}
		return ops;
	}

	/**
	 * 实际用于解析缓存注解，解析得到缓存操作的方法
	 * @param cachingConfig 缓存配置
	 * @param ae 缓存声明体，方法或类
	 * @param localOnly 是否本地声明覆盖接口声明
	 * @return
	 */
	@Nullable
	private Collection<CacheOperation> parseCacheAnnotations(DefaultCacheConfig cachingConfig, AnnotatedElement ae,
			boolean localOnly) {
		Collection<? extends Annotation> anns = (localOnly
				? AnnotatedElementUtils.getAllMergedAnnotations(ae, CACHE_OPERATION_ANNOTATIONS)
				: AnnotatedElementUtils.findAllMergedAnnotations(ae, CACHE_OPERATION_ANNOTATIONS));
		// 如果不存在注解声明
		if (anns.isEmpty()) {
			return null;
		}

		// 声明容量为1,不过ArrayList会自动扩容
		final Collection<CacheOperation> ops = new ArrayList<>(1);
		// 如果是@NeikoCacheable
		anns.stream().filter(ann -> ann instanceof NeikoCacheable).forEach(
				// 解析@NeikoCacheable返回CacheableOperation
				ann -> ops.add(parseCacheableAnnotation(ae, cachingConfig, (NeikoCacheable) ann)));
		// 如果是@NeikoCachePut
		anns.stream().filter(ann -> ann instanceof NeikoCachePut).forEach(
				// 解析@NeikoCacheable返回CacheableOperation
				ann -> ops.add(parseCachePutAnnotation(ae, cachingConfig, (NeikoCachePut) ann)));
		// 如果是@NeikoCacheEvict
		anns.stream().filter(ann -> ann instanceof NeikoCacheEvict).forEach(
				// 解析@NeikoCacheable返回CacheableOperation
				ann -> ops.add(parseCacheEvictAnnotation(ae, cachingConfig, (NeikoCacheEvict) ann)));
		return ops;

	}

	/**
	 * 解析@NeikoCacheable返回CacheableOperation
	 * @param ae
	 * @param defaultConfig
	 * @param cacheable
	 * @return
	 */
	private CacheableOperation parseCacheableAnnotation(AnnotatedElement ae, DefaultCacheConfig defaultConfig,
			NeikoCacheable cacheable) {

		CacheableOperation.Builder builder = new CacheableOperation.Builder();
		builder.setName(ae.toString());
		builder.setCacheRepository(cacheable.cacheRepository());
		builder.setCondition(cacheable.condition());
		builder.setUnless(cacheable.unless());
		builder.setKey(cacheable.key());
		builder.setKeyGenerator(cacheable.keyGenerator());
		builder.setSync(cacheable.sync());
		builder.setTtl(cacheable.ttl());
		builder.setUnit(cacheable.unit());

		defaultConfig.applyDefault(builder);
		CacheableOperation op = builder.build();
		// 简单校验参数：不能同时有key和keyGenerator
		validateCacheOperation(ae, op);

		return op;
	}

	/**
	 * 解析@NeikoCachePut返回CachePutOperation
	 * @param ae
	 * @param defaultConfig
	 * @param cachePut
	 * @return
	 */
	private CachePutOperation parseCachePutAnnotation(AnnotatedElement ae, DefaultCacheConfig defaultConfig,
			NeikoCachePut cachePut) {

		CachePutOperation.Builder builder = new CachePutOperation.Builder();
		builder.setName(ae.toString());
		builder.setCacheRepository(cachePut.cacheRepository());
		builder.setCondition(cachePut.condition());
		builder.setUnless(cachePut.unless());
		builder.setKey(cachePut.key());
		builder.setKeyGenerator(cachePut.keyGenerator());
		builder.setTtl(cachePut.ttl());
		builder.setUnit(cachePut.unit());
		builder.setSync(cachePut.sync());
		defaultConfig.applyDefault(builder);
		CachePutOperation op = builder.build();
		// 简单校验参数：不能同时有key和keyGenerator
		validateCacheOperation(ae, op);
		return op;
	}

	/**
	 * 解析@NeikoCacheEvict返回CacheEvictOperation
	 * @param ae
	 * @param defaultConfig
	 * @param cacheEvict
	 * @return
	 */
	private CacheEvictOperation parseCacheEvictAnnotation(AnnotatedElement ae, DefaultCacheConfig defaultConfig,
			NeikoCacheEvict cacheEvict) {

		CacheEvictOperation.Builder builder = new CacheEvictOperation.Builder();
		builder.setName(ae.toString());
		builder.setCacheRepository(cacheEvict.cacheRepository());
		builder.setCondition(cacheEvict.condition());
		builder.setKey(cacheEvict.key());
		builder.setKeyGenerator(cacheEvict.keyGenerator());
		builder.setSync(cacheEvict.sync());
		builder.setTtl(cacheEvict.ttl());
		builder.setUnit(cacheEvict.unit());
		defaultConfig.applyDefault(builder);
		CacheEvictOperation op = builder.build();
		// 简单校验参数：不能同时有key和keyGenerator
		validateCacheOperation(ae, op);

		return op;
	}

	/**
	 * 校验一下属性值
	 * @param ae
	 * @param operation
	 */
	private void validateCacheOperation(AnnotatedElement ae, CacheOperation operation) {
		if (StringUtils.hasText(operation.getKey()) && StringUtils.hasText(operation.getKeyGenerator())) {
			throw new IllegalStateException("Invalid cache annotation configuration on '" + ae.toString()
					+ "'. Both 'key' and 'keyGenerator' attributes have been set. "
					+ "These attributes are mutually exclusive: either set the SpEL expression used to"
					+ "compute the key at runtime or set the name of the KeyGenerator bean to use.");
		}
	}

	/**
	 * 注解属性
	 */
	private static class DefaultCacheConfig {

		private final Class<?> target;

		private String cacheRepository;

		private String keyGenerator;

		private String cacheManager;

		private String cacheResolver;

		private boolean initialized = false;

		public DefaultCacheConfig(Class<?> target) {
			this.target = target;
		}

		public void applyDefault(CacheOperation.Builder builder) {
			if (!this.initialized) {
				NeikoCacheConfig annotation = AnnotatedElementUtils.findMergedAnnotation(this.target,
						NeikoCacheConfig.class);
				if (annotation != null) {
					this.cacheRepository = annotation.cacheRepository();
					this.keyGenerator = annotation.keyGenerator();
					this.cacheManager = annotation.cacheManager();
					this.cacheResolver = annotation.cacheResolver();
				}
				this.initialized = true;
			}

			if (builder.getCacheRepository().isEmpty() && this.cacheRepository != null) {
				builder.setCacheRepository(this.cacheRepository);
			}
			if (!StringUtils.hasText(builder.getKey()) && !StringUtils.hasText(builder.getKeyGenerator())
					&& StringUtils.hasText(this.keyGenerator)) {
				builder.setKeyGenerator(this.keyGenerator);
			}
		}

	}

	@Data
	private final class MethodClassKey implements Comparable<MethodClassKey> {

		private final Method method;

		private final Class<?> targetClass;

		public MethodClassKey(Method method, Class<?> targetClass) {
			this.method = method;
			this.targetClass = targetClass;
		}

		@Override
		public boolean equals(@Nullable Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof MethodClassKey)) {
				return false;
			}
			MethodClassKey otherKey = (MethodClassKey) other;
			return (this.method.equals(otherKey.method)
					&& ObjectUtils.nullSafeEquals(this.targetClass, otherKey.targetClass));
		}

		@Override
		public int compareTo(MethodClassKey other) {
			int result = this.method.getName().compareTo(other.method.getName());
			if (result == 0) {
				result = this.method.toString().compareTo(other.method.toString());
				if (result == 0 && this.targetClass != null && other.targetClass != null) {
					result = this.targetClass.getName().compareTo(other.targetClass.getName());
				}
			}
			return result;
		}

	}

}
