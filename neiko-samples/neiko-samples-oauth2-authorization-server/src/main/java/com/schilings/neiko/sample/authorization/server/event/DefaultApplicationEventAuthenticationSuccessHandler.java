package com.schilings.neiko.sample.authorization.server.event;

import com.schilings.neiko.security.oauth2.authorization.server.handler.ApplicationEventAuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultApplicationEventAuthenticationSuccessHandler extends ApplicationEventAuthenticationSuccessHandler {
    
    public DefaultApplicationEventAuthenticationSuccessHandler(AuthenticationSuccessHandler delegate) {
        super(delegate);
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        getDelegate().onAuthenticationSuccess(request, response, authentication);
        getApplicationEventPublisher().publishEvent(new DemoEvent(authentication));
        
    }
    
    
}
