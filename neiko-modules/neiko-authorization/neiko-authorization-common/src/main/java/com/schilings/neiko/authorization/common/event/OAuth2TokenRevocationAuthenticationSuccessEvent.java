package com.schilings.neiko.authorization.common.event;


import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;

public class OAuth2TokenRevocationAuthenticationSuccessEvent extends ApplicationEvent {
    private final Authentication authentication;
    public OAuth2TokenRevocationAuthenticationSuccessEvent(Object source) {
        super(source);
        this.authentication = (Authentication) source;
    }

    public Authentication getAuthentication() {
        return authentication;
    }
}
