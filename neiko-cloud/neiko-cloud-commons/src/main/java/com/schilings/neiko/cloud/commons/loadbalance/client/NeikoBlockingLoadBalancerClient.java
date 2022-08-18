package com.schilings.neiko.cloud.commons.loadbalance.client;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;

import java.io.IOException;
import java.net.URI;

/**
 *
 * <p>
 * 这个类在有可能{@link org.springframework.cloud.loadbalancer.config.BlockingLoadBalancerClientAutoConfiguration}注入
 * </p>
 * <p>
 * 注入的前提是要有{@link org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory}
 * </p>
 * <p>
 * 那么工厂在这里注入{@link org.springframework.cloud.loadbalancer.config.LoadBalancerAutoConfiguration},
 * 所以@AutoConfigureAfter(LoadBalancerAutoConfiguration.class)
 * </p>
 * <p>
 * {@link org.springframework.cloud.loadbalancer.config.LoadBalancerAutoConfiguration}是spring-cloud-loadbalancer包下的，
 * commons同名的配置类{@link LoadBalancerAutoConfiguration}注的是{@link LoadBalancerRequestFactory}等request相关的
 * </p>
 *
 * <p>
 * {@link org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient}
 * </p>
 * <p>
 * {@link LoadBalancerProperties}
 * </p>
 * <p>
 * {@link LoadBalancerAutoConfiguration}
 * </p>
 * 这个配置会因为spring-cloud-loadbalancer注入LoadBalancerClient而生效
 * <p>
 * 忽略其他，主要是这个配置注入了{@link LoadBalancerRequestFactory}
 * </p>
 * Creates {@link LoadBalancerRequest}s for {@link LoadBalancerInterceptor}
 * and{@link RetryLoadBalancerInterceptor}. Applies
 * {@link LoadBalancerRequestTransformer}sto the intercepted {@link HttpRequest}.
 *
 * @author Schilings
 */
public class NeikoBlockingLoadBalancerClient implements LoadBalancerClient, ServiceInstanceChooser {

	@Override
	public <T> T execute(String serviceId, LoadBalancerRequest<T> request) throws IOException {
		return null;
	}

	@Override
	public <T> T execute(String serviceId, ServiceInstance serviceInstance, LoadBalancerRequest<T> request)
			throws IOException {
		return null;
	}

	@Override
	public URI reconstructURI(ServiceInstance instance, URI original) {
		return null;
	}

	@Override
	public ServiceInstance choose(String serviceId) {
		return null;
	}

	@Override
	public <T> ServiceInstance choose(String serviceId, Request<T> request) {
		return null;
	}

}
