package com.schilings.neiko.cloud.commons.loadbalance.client;

import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;

/**
 * <pre>
 * <p>{@link LoadBalancerInterceptor}</p>
 * </pre>
 *
 * @author Schilings
 */
public class NeikoLoadBalancerIntercaptor extends LoadBalancerInterceptor implements ClientHttpRequestInterceptor {

	// private LoadBalancerClient loadBalancer;

	// private LoadBalancerRequestFactory requestFactory;

	public NeikoLoadBalancerIntercaptor(LoadBalancerClient loadBalancer, LoadBalancerRequestFactory requestFactory) {
		super(loadBalancer, requestFactory);
	}

	public NeikoLoadBalancerIntercaptor(LoadBalancerClient loadBalancer) {
		super(loadBalancer);
	}

}
