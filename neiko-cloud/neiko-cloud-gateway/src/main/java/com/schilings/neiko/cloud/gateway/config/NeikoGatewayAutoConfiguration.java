package com.schilings.neiko.cloud.gateway.config;


import com.schilings.neiko.cloud.gateway.filter.gateway.BizLogicRouteGatewayFilterFactory;
import com.schilings.neiko.cloud.gateway.filter.global.I18nGlobalFilter;
import com.schilings.neiko.cloud.gateway.filter.global.LogGlobalFilter;
import com.schilings.neiko.cloud.gateway.filter.global.RouteRequestGlobalFilter;

import com.schilings.neiko.cloud.gateway.filter.httpheader.AuthTokenHttpHeadersFilter;
import com.schilings.neiko.cloud.gateway.predicate.CustomizeRoutePredicateFactory;
import com.schilings.neiko.cloud.gateway.properties.NeikoGatewayProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledFilter;
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledGlobalFilter;
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledPredicate;
import org.springframework.cloud.gateway.handler.FilteringWebHandler;
import org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping;
import org.springframework.cloud.gateway.route.RouteRefreshListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.DispatcherHandler;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "spring.cloud.gateway.neiko.enabled", matchIfMissing = true)
@EnableConfigurationProperties(NeikoGatewayProperties.class)
@AutoConfigureAfter(GatewayAutoConfiguration.class)
@Import(value = {RouteLocatorGatewayConfiguration.class,LoadBalancerGatewayConfiguration.class})
@RequiredArgsConstructor
public class NeikoGatewayAutoConfiguration {


    private final RoutePredicateHandlerMapping predicateHandlerMapping;
    
    private final FilteringWebHandler filteringWebHandler;

    @Autowired(required = false)
    private RouteRefreshListener routeRefreshListener;
    

    //HttpHeadersFilter beans

    @Bean
    @ConditionalOnProperty(name = "spring.cloud.gateway.neiko.token.enabled", matchIfMissing = true)
    public AuthTokenHttpHeadersFilter authTokenHttpHeadersFilter() {
        return new AuthTokenHttpHeadersFilter();
    }
    
    //GlobalFilter beans

    @Bean
    @ConditionalOnEnabledGlobalFilter
    public LogGlobalFilter logGlobalFilter() {
        return new LogGlobalFilter();
    }

    @Bean
    @ConditionalOnEnabledGlobalFilter
    public I18nGlobalFilter i18nGlobalFilter() {
        return new I18nGlobalFilter();
    }

    @Bean
    @ConditionalOnEnabledGlobalFilter
    public RouteRequestGlobalFilter routeRequestUrlGlobalFilter(ObjectProvider<DispatcherHandler> dispatcherHandlers) {
        return new RouteRequestGlobalFilter(dispatcherHandlers);
    }
    

    // GatewayFilter Factory beans

    @Bean
    @ConditionalOnEnabledFilter
    public BizLogicRouteGatewayFilterFactory bizLogicRouteGatewayFilterFactory() {
        return new BizLogicRouteGatewayFilterFactory();
    }

    // Predicate Factory beans
    
    @Bean
    @ConditionalOnEnabledPredicate
    public CustomizeRoutePredicateFactory customizeRoutePredicateFactory() {
        return new CustomizeRoutePredicateFactory();
    }
    
    
}
