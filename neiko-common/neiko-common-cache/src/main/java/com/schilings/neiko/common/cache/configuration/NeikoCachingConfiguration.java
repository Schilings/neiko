package com.schilings.neiko.common.cache.configuration;

import com.schilings.neiko.common.cache.advisor.NeikoCacheAdvisor;
import com.schilings.neiko.common.cache.advisor.NeikoCacheInterceptor;
import com.schilings.neiko.common.cache.components.*;
import com.schilings.neiko.common.cache.parser.NeikoCacheAnnotationParser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 * <p>Neiko缓存的配置</p>
 * </pre>
 *
 * @author Schilings
 */
@Configuration(proxyBeanMethods = false)
public class NeikoCachingConfiguration extends AbstractCachingConfiguration {

	@Bean
	public NeikoCacheAdvisor cacheAdvisor(NeikoCacheInterceptor interceptor) {
		NeikoCacheAdvisor advisor = new NeikoCacheAdvisor();
		advisor.setAdvice(interceptor);
		// 设置这个切入Advisor的优先级
		if (this.enableCaching != null) {
			advisor.setOrder(this.enableCaching.<Integer>getNumber("order"));
		}
		return advisor;
	}

	@Bean
	public NeikoCacheInterceptor cacheInterceptor() {
		NeikoCacheInterceptor interceptor = new NeikoCacheInterceptor();
		interceptor.setAnnotationParser(new NeikoCacheAnnotationParser());
		interceptor.setErrorHandler(new SimpleCacheErrorHandler());
		return interceptor;
	}

}
