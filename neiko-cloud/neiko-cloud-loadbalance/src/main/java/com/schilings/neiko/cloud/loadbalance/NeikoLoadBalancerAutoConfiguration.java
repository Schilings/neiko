package com.schilings.neiko.cloud.loadbalance;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalancerLifecycle;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;


/**
 *<p>{@link LoadBalancerClientFactory}引入{@link org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration}</p>
 */

@AutoConfiguration
@RequiredArgsConstructor
@LoadBalancerClients(defaultConfiguration = {NeikoLoadBalancerClientConfiguration.class})
public class NeikoLoadBalancerAutoConfiguration {

    private final ReactorServiceInstanceLoadBalancer loadBalancer;

    private final LoadBalancerClientFactory loadBalancerClientFactory;

    private final ServiceInstanceListSupplier serviceInstanceListSupplier;

    //require=false
    private LoadBalancerLifecycle loadBalancerLifecycle;
    
    



    
}
