package com.schilings.neiko.cloud.gateway.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.schilings.neiko.cloud.gateway.handler.RequestBodyRewrite;
import com.schilings.neiko.cloud.gateway.handler.ResponseBodyRewrite;
import com.schilings.neiko.cloud.gateway.route.RouteLocatorDelegate;
import com.schilings.neiko.cloud.gateway.route.RouteOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RouteLocatorGatewayConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RouteOperator routeOperator(RouteDefinitionWriter routeDefinitionWriter,
                                       ApplicationEventPublisher applicationEventPublisher) {
        return new RouteOperator(routeDefinitionWriter, applicationEventPublisher);
    }
    

    @Bean
    @ConditionalOnMissingBean
    public RouteLocatorDelegate routeLocatorDelege(RouteLocator routeLocator, 
                                                   RouteDefinitionRepository routeDefinitionRepository,
                                                   RouteDefinitionLocator routeDefinitionLocator) {
        return new RouteLocatorDelegate(routeDefinitionRepository, routeDefinitionLocator, routeLocator);
    }

    @Bean
    public RouteLocator customizeRoute(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(
                        // 第一个参数是路由的唯一身份
                        "path_route_lb",
                        // 第二个参数是个lambda实现，
                        // 设置了配套条件是按照请求路径匹配，以及转发地址，
                        // 注意lb://表示这是个服务名，要从
                        r -> r.path("/lbtest/**").uri("lb://provider-hello")
                )
                .build();
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, @Autowired(required = false) ObjectMapper objectMapper) {
        return builder
                .routes()
                .route("path_route_change",
                        p -> p.cloudFoundryRouteService().and()//CloudFoundryRouteServiceRoutePredicateFactory
                                .path("/hello/change")//PathRoutePredicateFactory
                                .filters(f -> f 
                                                //ModifyRequestBodyGatewayFilterFactory
                                                .modifyRequestBody(String.class,String.class,new RequestBodyRewrite(objectMapper))
                                                  //ModifyResponseBodyGatewayFilterFactory
                                                .modifyResponseBody(String.class, String.class, new ResponseBodyRewrite(objectMapper))
                                        //还有很多
                                        //.addRequestHeader()
                                        //.addRequestParameter()
                                        //.addResponseHeader()
                                        //.fallbackHeaders()
                                        //.changeRequestUri()
                                )
                                .uri("http://127.0.0.1:8082"))
                .build();
    }
}
