package com.schilings.neiko.cloud.commons.http.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.client.loadbalancer.ResponseData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.net.URI;

/**
 * <pre>
 * <p>{@link LoadBalancerInterceptor}</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
public abstract class ClientHttpRequestInterceptorEnhancer implements ClientHttpRequestInterceptor {

	private URI originalUri;

	private HttpMethod method;

	private HttpHeaders headers;

	private String methodValue;

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		this.originalUri = request.getURI();
		this.method = request.getMethod();
		this.headers = request.getHeaders();
		this.methodValue = request.getMethod().name();

		//
		headers.add("Cookie", "SESSIONID=b8dd5bd9-9fb7-48cb-a86b-e079cb554fb8");
		log.info("拦截器已添加header");
		//
		// 继续执行链，拿到响应结果
		ClientHttpResponse httpResponse = interceptInternal(request, body, execution);
		// 处理httpResponse

		// Object response = getClientResponse(httpResponse);

		return httpResponse;
	}

	public abstract ClientHttpResponse interceptInternal(HttpRequest request, byte[] body,
			ClientHttpRequestExecution execution) throws IOException;

	// ResponseData引入import
	// org.springframework.web.reactive.function.client.ClientResponse;
	// 我这里没引入这个包
	// private <T> Object getClientResponse(T response) {
	// ClientHttpResponse clientHttpResponse = null;
	// if (response instanceof ClientHttpResponse) {
	// clientHttpResponse = (ClientHttpResponse) response;
	// }
	// if (clientHttpResponse != null) {
	// try {
	// return new ResponseData(clientHttpResponse, null);
	// }
	// catch (IOException ignored) {
	// }
	// }
	// return response;
	// }

}
