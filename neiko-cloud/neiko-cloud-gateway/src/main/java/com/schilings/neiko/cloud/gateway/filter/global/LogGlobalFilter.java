package com.schilings.neiko.cloud.gateway.filter.global;

import cn.hutool.core.map.MapUtil;
import com.schilings.neiko.cloud.gateway.properties.NeikoGatewayProperties;
import com.schilings.neiko.common.util.StringUtils;
import com.schilings.neiko.common.util.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 *
 * <p>
 * 全局日志过滤器
 * </p>
 * <p>
 * 用于打印请求执行参数与响应时间等等
 * </p>
 *
 * @author Schilings
 */
@Slf4j
public class LogGlobalFilter implements GlobalFilter, Ordered {

	@Autowired
	private NeikoGatewayProperties neikoGatewayProperties;

	private static final String START_TIME = "startTime";

	// 例如.ForwardRoutingFilter
	// ForwardRoutingFilter 在 exchange
	// 中找到属性ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR
	// 获得
	// 如果 URL 具有转发模式(例如 forward:///localendpoint) ，则使用 Spring DispatcherHandler 来处理请求。
	// 请求 URL 的路径部分被转发 URL 中的路径所覆盖。未修改的原始 URL
	// 附加到ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR属性的列表中。

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String path = getOriginalRequestUrl(exchange);
		String url = request.getMethod().name() + " " + path;

		if (!neikoGatewayProperties.getRequestLog()) {
			return chain.filter(exchange);
		}
		// 打印请求参数
		if (isJsonRequest(request)) {
			String jsonParam = resolveBodyFromRequest(request);
			log.info("[PLUS]开始请求 => URL[{}],参数类型[json],参数:[{}]", url, jsonParam);
		}
		else {
			MultiValueMap<String, String> parameterMap = request.getQueryParams();
			if (MapUtil.isNotEmpty(parameterMap)) {
				String parameters = JsonUtils.toJson(parameterMap);
				log.info("[PLUS]开始请求 => URL[{}],参数类型[param],参数:[{}]", url, parameters);
			}
			else {
				log.info("[PLUS]开始请求 => URL[{}],无参数", url);
			}
		}

		exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			Long startTime = exchange.getAttribute(START_TIME);
			if (startTime != null) {
				long executeTime = (System.currentTimeMillis() - startTime);
				log.info("[PLUS]结束请求 => URL[{}],耗时:[{}]毫秒", url, executeTime);
			}
		}));
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	/**
	 * 判断本次请求的数据类型是否为json
	 * @param request request
	 * @return boolean
	 */
	private boolean isJsonRequest(ServerHttpRequest request) {
		MediaType contentType = request.getHeaders().getContentType();
		if (contentType != null) {
			return StringUtils.startsWithIgnoreCase(contentType.toString(), MediaType.APPLICATION_JSON_VALUE);
		}
		// return request.getMethod().matches(HttpMethod.POST.name());
		return false;
	}

	private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
		// 获取请求体
		Flux<DataBuffer> body = serverHttpRequest.getBody();
		StringBuilder sb = new StringBuilder();
		body.subscribe(buffer -> {
			byte[] bytes = new byte[buffer.readableByteCount()];
			buffer.read(bytes);
			String bodyString = new String(bytes, StandardCharsets.UTF_8);
			sb.append(bodyString);
		});
		return sb.toString();
	}

	public static String getOriginalRequestUrl(ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
		// 网关原始请求 URL 属性名称。
		// LinkedHashSet<URI> uris =
		// exchange.getRequiredAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
		LinkedHashSet<URI> uris = exchange.getAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
		if (!CollectionUtils.isEmpty(uris)) {
			URI requestUri = uris.stream().findFirst().orElse(request.getURI());
			return UriComponentsBuilder.fromPath(requestUri.getRawPath()).build().toUriString();
		}
		// 否则
		return UriComponentsBuilder.fromPath(exchange.getRequest().getURI().getRawPath()).build().toUriString();
	}

}
