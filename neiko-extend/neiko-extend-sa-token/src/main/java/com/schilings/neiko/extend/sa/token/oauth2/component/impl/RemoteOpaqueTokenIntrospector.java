package com.schilings.neiko.extend.sa.token.oauth2.component.impl;


import com.schilings.neiko.extend.sa.token.oauth2.component.OpaqueTokenIntrospector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class RemoteOpaqueTokenIntrospector implements OpaqueTokenIntrospector {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Converter<String, RequestEntity<?>> requestEntityConverter;

    private RestOperations restOperations;

    private static final String AUTHORITY_SCOPE_PREFIX = "SCOPE_";

    /**
     * Creates a {@code OpaqueTokenAuthenticationProvider} with the provided parameters
     * @param introspectionUri The introspection endpoint uri
     * @param clientId The client id authorized to introspect
     * @param clientSecret The client's secret
     */
    public RemoteOpaqueTokenIntrospector(String introspectionUri, String clientId, String clientSecret) {
        Assert.notNull(introspectionUri, "introspectionUri cannot be null");
        Assert.notNull(clientId, "clientId cannot be null");
        Assert.notNull(clientSecret, "clientSecret cannot be null");
        this.requestEntityConverter = this.defaultRequestEntityConverter(URI.create(introspectionUri));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(clientId, clientSecret));
        this.restOperations = restTemplate;
    }
    


    private Converter<String, RequestEntity<?>> defaultRequestEntityConverter(URI introspectionUri) {
        return token -> {
            HttpHeaders headers = requestHeaders();
            MultiValueMap<String, String> body = requestBody(token);
            return new RequestEntity<>(body, headers, HttpMethod.POST, introspectionUri);
        };
    }


    private HttpHeaders requestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
        return headers;
    }

    private MultiValueMap<String, String> requestBody(String token) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", token);
        return body;
    }

    @Override
    public Object introspect(String token) {
        return null;
    }
}
