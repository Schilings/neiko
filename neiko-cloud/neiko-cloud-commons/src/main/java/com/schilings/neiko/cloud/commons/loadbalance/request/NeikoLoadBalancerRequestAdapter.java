package com.schilings.neiko.cloud.commons.loadbalance.request;


import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequestAdapter;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequestFactory;

/**
 * <pre>
 * <p>{@link LoadBalancerRequestAdapter}</p>
 * <p>{@link LoadBalancerRequestFactory}</p>
 * </pre>
 * @author Schilings
*/
public class NeikoLoadBalancerRequestAdapter<T,DC> extends LoadBalancerRequestAdapter<T,DC>{
    public NeikoLoadBalancerRequestAdapter(LoadBalancerRequest<T> delegate) {
        super(delegate);
    }

    public NeikoLoadBalancerRequestAdapter(LoadBalancerRequest<T> delegate, DC context) {
        super(delegate, context);
    }
}
