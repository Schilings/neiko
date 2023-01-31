package com.schilings.neiko.sample.resource.server.config;


import com.schilings.neiko.security.oauth2.resource.server.autoconfigure.EnableResourceServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity(debug = true)
@EnableResourceServer
public class ResourceServerConfig {
    
}
