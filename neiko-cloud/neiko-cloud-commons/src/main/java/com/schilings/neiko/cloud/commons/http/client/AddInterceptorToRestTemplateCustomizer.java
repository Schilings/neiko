package com.schilings.neiko.cloud.commons.http.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <pre>
 * <p>{@link LoadBalancerAutoConfiguration}</p>
 * </pre>
 *
 * @author Schilings
 */
@RequiredArgsConstructor
//@Configuration
public class AddInterceptorToRestTemplateCustomizer {

	@LoadBalanced
	@Autowired(required = false)
	private List<RestTemplate> restTemplates = Collections.emptyList();

	@Bean
	@ConditionalOnMissingBean
	public ClientHttpRequestInterceptorEnhancer interceptorEnhancer() {
		return new ClientHttpRequestInterceptorEnhancer() {
			@Override
			public ClientHttpResponse interceptInternal(HttpRequest request, byte[] body,
					ClientHttpRequestExecution execution) throws IOException {
				return execution.execute(request, body);
			}
		};
	}

	@Bean
	@ConditionalOnMissingBean
	public RestTemplateCustomizer restTemplateCustomizer(
			final ClientHttpRequestInterceptorEnhancer interceptorEnhancer) {
		return restTemplate -> {
			List<ClientHttpRequestInterceptor> list = new ArrayList<>(restTemplate.getInterceptors());
			list.add(interceptorEnhancer);
			restTemplate.setInterceptors(list);
		};
	}

}
