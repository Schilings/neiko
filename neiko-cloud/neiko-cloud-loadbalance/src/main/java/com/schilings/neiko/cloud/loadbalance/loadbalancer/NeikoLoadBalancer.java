package com.schilings.neiko.cloud.loadbalance.loadbalancer;


import com.schilings.neiko.cloud.commons.utils.NetworkUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.loadbalancer.config.LoadBalancerAutoConfiguration;

import org.springframework.cloud.loadbalancer.core.*;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map(serviceInstances -> processInstanceResponse(supplier, serviceInstances));
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
                                                              List<ServiceInstance> serviceInstances) {
        Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(serviceInstances);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + serviceId);
            }
            return new EmptyResponse();
        }
        for (ServiceInstance instance : instances) {
            if (instance.getHost().equals(NetworkUtils.getLocalHostname())) {
                return new DefaultResponse(instance);
            }
        }
        return new DefaultResponse(instances.get(ThreadLocalRandom.current().nextInt(instances.size())));
    }
}
