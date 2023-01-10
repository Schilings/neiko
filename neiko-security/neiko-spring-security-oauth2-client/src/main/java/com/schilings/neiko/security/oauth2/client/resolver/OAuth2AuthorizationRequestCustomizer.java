package com.schilings.neiko.security.oauth2.client.resolver;


import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import javax.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface OAuth2AuthorizationRequestCustomizer {

    void customize(HttpServletRequest request, OAuth2AuthorizationRequest.Builder builder);

}
