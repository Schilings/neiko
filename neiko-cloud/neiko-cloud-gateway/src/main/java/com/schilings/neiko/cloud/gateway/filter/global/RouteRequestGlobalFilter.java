package com.schilings.neiko.cloud.gateway.filter.global;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.*;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.regex.Pattern;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

/**
 *
 * <p>
 * {@link RouteToRequestUrlFilter}
 * </p>
 * <p>
 * {@link ForwardRoutingFilter}
 * </p>
 * <p>
 * {@link ForwardPathFilter}
 * </p>
 *
 * @author Schilings
 */
public class RouteRequestGlobalFilter implements GlobalFilter, Ordered {

	// @Bean
	// @ConditionalOnEnabledGlobalFilter
	// public GlobalFilter customGlobalFilter1() {
	// return (exchange, chain) -> exchange.getPrincipal()
	// .map(Principal::getName)
	// .defaultIfEmpty("Default User")
	// .map(userName -> {
	// //adds header to proxied request
	// exchange.getRequest().mutate().header("CUSTOM-REQUEST-HEADER", userName).build();
	// return exchange;
	// })
	// .flatMap(chain::filter);
	// }
	//
	// @Bean
	// public GlobalFilter customGlobalPostFilter() {
	// return (exchange, chain) -> chain.filter(exchange)
	// .then(Mono.just(exchange))
	// .map(serverWebExchange -> {
	// //adds header to response
	// serverWebExchange.getResponse().getHeaders().set("CUSTOM-RESPONSE-HEADER",
	// HttpStatus.OK.equals(serverWebExchange.getResponse().getStatusCode()) ? "It
	// worked": "It did not work");
	// return serverWebExchange;
	// })
	// .then();
	// }

	// RouteToRequestUrlFilter的后一位
	public static final int ORDER = 10001;

	private static final Log log = LogFactory.getLog(RouteRequestGlobalFilter.class);

	private static final String SCHEME_REGEX = "[a-zA-Z]([a-zA-Z]|\\d|\\+|\\.|-)*:.*";
	static final Pattern schemePattern = Pattern.compile(SCHEME_REGEX);

	private final ObjectProvider<DispatcherHandler> dispatcherHandlerProvider;

	// do not use this dispatcherHandler directly, use getDispatcherHandler() instead.
	private volatile DispatcherHandler dispatcherHandler;

	public RouteRequestGlobalFilter(ObjectProvider<DispatcherHandler> dispatcherHandlerProvider) {
		this.dispatcherHandlerProvider = dispatcherHandlerProvider;
	}

	private DispatcherHandler getDispatcherHandler() {
		if (dispatcherHandler == null) {
			dispatcherHandler = dispatcherHandlerProvider.getIfAvailable();
		}

		return dispatcherHandler;
	}

	@Override
	public int getOrder() {
		return ORDER;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// 路由
		Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
		if (route == null) {
			return chain.filter(exchange);
		}
		// 配置的路由路径 http://127.0.0.1:9999
		URI routeUri = route.getUri();
		// 处理后的uri，裁剪切割等 http://localhost:8888/demo/str
		URI uri = exchange.getRequest().getURI();
		// RouteToRequestUrlFilter会将解析后路径设置
		URI requestUrl = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);

		if ("lb".equalsIgnoreCase(routeUri.getScheme()) && routeUri.getHost() == null) {
			// 负载平衡的 URI 应该始终有一个主机。如果主机为空，很可能是因为主机名无效（例如包含下划线）
			throw new IllegalStateException("Invalid host: " + routeUri.toString());
		}

		return chain.filter(exchange);
	}

}
