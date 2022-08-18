package com.schilings.neiko.cloud.commons.loadbalance.request.wrapper;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.ServiceRequestWrapper;
import org.springframework.http.HttpRequest;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author Schilings
 */
public class NeikoServiceRequestWrapper extends ServiceRequestWrapper {

	public NeikoServiceRequestWrapper(HttpRequest request, ServiceInstance instance, LoadBalancerClient loadBalancer) {
		super(request, instance, loadBalancer);
	}

}
