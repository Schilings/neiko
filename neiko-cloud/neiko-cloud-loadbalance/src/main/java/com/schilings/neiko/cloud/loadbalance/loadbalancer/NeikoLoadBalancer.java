package com.schilings.neiko.cloud.loadbalance.loadbalancer;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.loadbalancer.config.LoadBalancerAutoConfiguration;

import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import reactor.core.publisher.Mono;

/**
 * <p>负载均衡器</p>
 * <p>{@link LoadBalancerClientFactory}引入{@link LoadBalancerClientConfiguration}</p>
 * <p>
 * {@link LoadBalancerClientConfiguration#reactorServiceInstanceLoadBalancer(org.springframework.core.env.Environment, org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory)}
 *  <p>这个是默认的ReactiveLoadBalancer 实现类，默认的负载均衡策略</p>
 * <hr></hr>
 *  {@link LoadBalancerClientConfiguration}
 *  {@link LoadBalancerAutoConfiguration}
 * <hr></hr>
 * <p>下面两个配置类，挺重要的，看好它注入的那些bean的条件</p>
 * {@link LoadBalancerClientConfiguration.ReactiveSupportConfiguration}
 * <p>这个是响应Web框架下的注入的服务实例列表配置类</p>
 *  {@link LoadBalancerClientConfiguration.BlockingSupportConfiguration}
 *   <p>这个是阻塞Web框架下的注入的服务实例列表配置类</p>
 * @author Schilings
*/
public class NeikoLoadBalancer implements ReactorServiceInstanceLoadBalancer,ReactorLoadBalancer<ServiceInstance> {

    private static final Log log = LogFactory.getLog(NeikoLoadBalancer.class);

    private final String serviceId;

    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;


    public NeikoLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                             String serviceId) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        return Mono.empty();
    }
}
