package com.schilings.neiko.cloud.commons.loadbalance.lifecycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;

/**
 * <pre>
 *     <p>直接注入即可</p>
 * <p>{@link LoadBalancerLifecycle}</p>
 * <p>{@link LoadBalancerLifecycleValidator}</p>
 * </pre>
 *
 * @author Schilings
 */

@Slf4j
public class NeikoLoadBalancerLifeCycle implements LoadBalancerLifecycle<HintRequestContext, Object, ServiceInstance> {

	@Override
	public boolean supports(Class requestContextClass, Class responseClass, Class serverTypeClass) {
		return LoadBalancerLifecycle.super.supports(requestContextClass, responseClass, serverTypeClass);
	}

	@Override
	public void onStart(Request<HintRequestContext> request) {

	}

	@Override
	public void onStartRequest(Request<HintRequestContext> request, Response<ServiceInstance> lbResponse) {

	}

	@Override
	public void onComplete(CompletionContext<Object, ServiceInstance, HintRequestContext> completionContext) {

	}

}
