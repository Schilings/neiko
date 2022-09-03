package com.schilings.neiko.auth.handler;


import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@FunctionalInterface
public interface InterceptorsRegistration {
    
    void register(InterceptorRegistry registry);
    
}
