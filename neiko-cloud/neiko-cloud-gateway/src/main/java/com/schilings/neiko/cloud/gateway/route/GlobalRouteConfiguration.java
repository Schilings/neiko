package com.schilings.neiko.cloud.gateway.route;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.schilings.neiko.cloud.gateway.custom.RequestBodyRewrite;
import com.schilings.neiko.cloud.gateway.custom.ResponseBodyRewrite;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

public class GlobalRouteConfiguration {


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
    public RouteLocator routes(RouteLocatorBuilder builder, ObjectMapper objectMapper) {
        return builder
                .routes()
                .route("path_route_change",
                        r -> r.path("/hello/change")
                                .filters(f -> f
                                                .modifyRequestBody(String.class,String.class,new RequestBodyRewrite(objectMapper))
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
