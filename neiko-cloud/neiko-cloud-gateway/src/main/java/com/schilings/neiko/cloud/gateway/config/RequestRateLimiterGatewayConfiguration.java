package com.schilings.neiko.cloud.gateway.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.context.annotation.Configuration;


/**\
 * 指定限流维度的代码CustomizeConfig.java，这里是根据请求参数username的值来限流的，
 * 假设真实请求中一半请求的username的等于Tom，另一半的username的等于Jerry，
 * 按照application.yml的配置，Tom的请求QPS为10，Jerry的QPS也是10：
 */
@Configuration(proxyBeanMethods = false)
public class RequestRateLimiterGatewayConfiguration {


    @Autowired(required = false)
    private RateLimiter rateLimiter;

    @Autowired(required = false)
    private KeyResolver keyResolver;

    @Autowired(required = false)
    private RequestRateLimiterGatewayFilterFactory requestRateLimiterGatewayFilterFactory;
    
//    @Bean
//    @ConditionalOnMissingBean(KeyResolver.class)
//    KeyResolver userKeyResolver() {
//        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("username"));
//    }
    
}
