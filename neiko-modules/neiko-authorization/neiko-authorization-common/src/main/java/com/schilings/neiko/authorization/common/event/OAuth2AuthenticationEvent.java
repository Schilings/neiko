package com.schilings.neiko.authorization.common.event;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public abstract class OAuth2AuthenticationEvent extends ApplicationEvent {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private final Authentication authentication;
    private Map<String, Object> attributes;

    public OAuth2AuthenticationEvent(Authentication authentication) {
        this(null, null, authentication, Collections.emptyMap());
    }

    public OAuth2AuthenticationEvent(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response,
                                     Authentication authentication) {
        this(request, response, authentication, Collections.emptyMap());
    }

    public OAuth2AuthenticationEvent(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response,
                                     Authentication authentication, Map<String, Object> attributes) {
        super(authentication);
        Assert.notNull(authentication, "authentication can not be null.");
        Assert.notNull(attributes, "attributes can not be null.");
        this.authentication = authentication;
        this.request = request;
        this.response = response;
        this.attributes = Collections.unmodifiableMap(attributes);
    }


    public Authentication getAuthentication() {
        return authentication;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }


    
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String name) {
        Assert.hasText(name, "name cannot be empty");
        return (T) this.attributes.get(name);
    }
    
}
