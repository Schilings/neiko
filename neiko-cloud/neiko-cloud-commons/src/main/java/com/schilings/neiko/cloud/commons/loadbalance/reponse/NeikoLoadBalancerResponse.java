package com.schilings.neiko.cloud.commons.loadbalance.reponse;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author Schilings
 */
public class NeikoLoadBalancerResponse extends DefaultResponse {

	public NeikoLoadBalancerResponse(ServiceInstance serviceInstance) {
		super(serviceInstance);
	}

}
