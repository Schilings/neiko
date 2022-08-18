package com.schilings.neiko.cloud.gateway.filter.httpheader;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.headers.ForwardedHeadersFilter;
import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter;
import org.springframework.cloud.gateway.filter.headers.RemoveHopByHopHeadersFilter;
import org.springframework.cloud.gateway.filter.headers.XForwardedHeadersFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;


/**
 * 
 * <p>{@link RemoveHopByHopHeadersFilter}</p>
 * <p>{@link XForwardedHeadersFilter}</p>
 * <p>{@link ForwardedHeadersFilter}</p>
 * <p>{@link HttpHeadersFilter#filterRequest(java.util.List, org.springframework.web.server.ServerWebExchange)}</p>
 * 
 * @author Schilings
*/
@Slf4j
public class AuthTokenHttpHeadersFilter implements HttpHeadersFilter, Ordered {


    /**
     * 顺序
     */
    private int order = Ordered.LOWEST_PRECEDENCE;

    /**
     * 是否启动
     */
    private boolean enabled = true;

    @Override
    public boolean supports(Type type) {
        //过滤Response
        //默认过滤的是Request
        return type.equals(Type.RESPONSE);
    }
    
    @Override
    public HttpHeaders filter(HttpHeaders input, ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders original = input;
        HttpHeaders updated = new HttpHeaders();
        //在响应体上添加header
        input.add("auth-token", "test header token");
        return input;
    }
    

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
