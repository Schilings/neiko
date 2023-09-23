package com.schilings.neiko.sample.oauth2.config;

import com.schilings.neiko.security.oauth2.resource.server.autoconfigure.EnableResourceServer;

import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableResourceServer
public class ResourceServerConfiguration {

}
