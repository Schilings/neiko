package com.schilings.neiko.sample.oauth2.config;
import com.schilings.neiko.security.oauth2.resource.server.config.EnableResourceServer;


import com.schilings.neiko.security.oauth2.resource.server.exception.DefaultAccessDeniedHandler;
import com.schilings.neiko.security.oauth2.resource.server.exception.DefaultAuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;


@Configuration(proxyBeanMethods = false)
@EnableResourceServer
public class ResourceServerConfiguration{
    
    
}
