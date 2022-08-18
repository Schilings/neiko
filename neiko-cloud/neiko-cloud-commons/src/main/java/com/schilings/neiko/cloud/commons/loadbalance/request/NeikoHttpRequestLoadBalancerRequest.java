package com.schilings.neiko.cloud.commons.loadbalance.request;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.HttpRequestLoadBalancerRequest;
import org.springframework.http.HttpRequest;

/**
 *
 * <p>
 * {@link org.springframework.cloud.client.loadbalancer.BlockingLoadBalancerRequest}
 * </p>
 *
 * @author Schilings
 */
public class NeikoHttpRequestLoadBalancerRequest implements HttpRequestLoadBalancerRequest<ServiceInstance> {

	@Override
	public HttpRequest getHttpRequest() {
		return null;
	}

	@Override
	public ServiceInstance apply(ServiceInstance instance) throws Exception {
		return null;
	}

}
