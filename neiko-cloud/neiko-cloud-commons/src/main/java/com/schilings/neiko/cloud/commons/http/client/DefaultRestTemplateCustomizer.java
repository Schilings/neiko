package com.schilings.neiko.cloud.commons.http.client;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.web.client.RestTemplate;

/**
 * <pre>
 * <p>{@link LoadBalancerAutoConfiguration}直接注入就会对负载均衡的RestTemplate生效</p>
 * </pre>
 * @author Schilings
*/
@Slf4j
public class DefaultRestTemplateCustomizer implements RestTemplateCustomizer {

    @Autowired(required = false)
    private LoadBalancerClient loadBalancerClient;

    @Autowired(required = false)//LoadBalancerClient继承ServiceInstanceChooser
    private ServiceInstanceChooser chooser;

    @Autowired
    private LoadBalancerRequestFactory loadBalancerRequestFactory;

    @Autowired
    private LoadBalancerInterceptor loadBalancerInterceptor;
    
    @Override
    public void customize(RestTemplate restTemplate) {
        
    }
}
