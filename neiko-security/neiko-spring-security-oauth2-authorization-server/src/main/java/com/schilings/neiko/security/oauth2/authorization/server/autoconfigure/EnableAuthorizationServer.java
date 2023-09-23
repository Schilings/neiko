package com.schilings.neiko.security.oauth2.authorization.server.autoconfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AuthorizationServerConfigurationAdapter.class)
public @interface EnableAuthorizationServer {

}
