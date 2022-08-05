package com.schilings.neiko.cloud.loadbalance;


import com.schilings.neiko.cloud.loadbalance.loadbalancer.NeikoLoadBalancer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * LoadBalancerClient默认配置
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnDiscoveryEnabled//注册发现
public class NeikoLoadBalancerClientConfiguration {

    /**
     * {@link org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration }</p>
     * @param environment
     * @param loadBalancerClientFactory
     * @return
     */
//    @Bean
//    @ConditionalOnMissingBean
//    ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
//                                                            LoadBalancerClientFactory loadBalancerClientFactory) {
//        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
//        return new RandomLoadBalancer(loadBalancerClientFactory
//                .getLazyProvider(name, ServiceInstanceListSupplier.class),
//                name);
//    }

    /**
     * 自定义负载均衡算法
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(LoadBalancerClientFactory.class)
    public ReactorLoadBalancer<ServiceInstance> customLoadBalancer(Environment environment,
                                                                   LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new NeikoLoadBalancer(loadBalancerClientFactory
                .getLazyProvider(name, ServiceInstanceListSupplier.class),
                name);
    }
}
