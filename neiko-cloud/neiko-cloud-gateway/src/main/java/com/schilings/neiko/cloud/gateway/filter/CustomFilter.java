package com.schilings.neiko.cloud.gateway.filter;


import lombok.extern.slf4j.Slf4j;


/**
 * GatewayAutoConfiguration中对过滤器的注入，都是这两个注解。
 * 显然我们无法顶替原先的内部过滤器，就先不去尝试实现
 * @ Bean
 * @ ConditionalOnEnabledGlobalFilter 这个注解的实现原理可以学习一下
 *    
 * @see org.springframework.cloud.gateway.config.GatewayAutoConfiguration 里面还有很多注入的bean，去看！值得看！
 */

@Slf4j 
public class CustomFilter {
    //例如.ForwardRoutingFilter  
    //ForwardRoutingFilter 在 exchange 中找到属性ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR
    //获得
    //如果 URL 具有转发模式(例如 forward:///localendpoint) ，则使用 Spring DispatcherHandler 来处理请求。
    //请求 URL 的路径部分被转发 URL 中的路径所覆盖。未修改的原始 URL 附加到ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR属性的列表中。
    

    
    
}
