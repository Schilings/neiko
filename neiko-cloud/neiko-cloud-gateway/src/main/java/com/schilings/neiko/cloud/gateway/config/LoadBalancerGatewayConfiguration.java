package com.schilings.neiko.cloud.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayReactiveLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.context.annotation.Configuration;

/**
 * <p>{@link GatewayReactiveLoadBalancerClientAutoConfiguration}</p>
 * <p>{@link ReactiveLoadBalancerClientFilter}</p>
 * 
 * @author Schilings
*/
@Configuration(proxyBeanMethods = false)
public class LoadBalancerGatewayConfiguration {

    @Autowired(required = false)
    private ReactiveLoadBalancerClientFilter reactiveLoadBalancerClientFilter;
}
