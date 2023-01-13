package com.schilings.neiko.security.oauth2.resource.server.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ResourceServerConfigurationAdapter.class)
public @interface EnableResourceServer {

}
