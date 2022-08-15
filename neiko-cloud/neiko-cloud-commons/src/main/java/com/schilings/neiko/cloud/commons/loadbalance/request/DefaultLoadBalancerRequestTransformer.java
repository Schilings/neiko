package com.schilings.neiko.cloud.commons.loadbalance.request;


import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     <p>{@link LoadBalancerRequestFactory#createRequest(org.springframework.http.HttpRequest, byte[], org.springframework.http.client.ClientHttpRequestExecution)}</p>
 *     <p>{@link BlockingLoadBalancerRequest#apply(org.springframework.cloud.client.ServiceInstance)}</p>
 *  <p>直接注入即可，{@link LoadBalancerAutoConfiguration}</p>
 * <p>允许应用程序在给定所选ServiceInstance的情况下转换负载平衡的HttpRequest </p>
 * <p>{@link org.springframework.cloud.loadbalancer.core.LoadBalancerServiceInstanceCookieTransformer}</p>
 * </pre>
 * @author Schilings
*/
@RequiredArgsConstructor
//@ConditionalOnBean(LoadBalancerClientFactory.class)
public class DefaultLoadBalancerRequestTransformer implements LoadBalancerRequestTransformer {

    //LoadBalancerClientFactory implements ReactiveLoadBalancer.Factory<ServiceInstance>
    private final ReactiveLoadBalancer.Factory<ServiceInstance> factory;
    
    @Override
    public HttpRequest transformRequest(HttpRequest request, ServiceInstance instance) {
        if (instance == null) {
            return request;
        }
        return request;
    }
}
