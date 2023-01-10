package com.schilings.neiko.security.oauth2.authorization.server.customizer;


import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerConfigurerCustomizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenIntrospectionEndpointConfigurer;

public abstract class OAuth2TokenIntrospectionEndpointConfigurerCustomizer implements OAuth2AuthorizationServerConfigurerCustomizer {
    
    @Override
    public void customize(OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer, HttpSecurity httpSecurity) throws Exception {
        oAuth2AuthorizationServerConfigurer.tokenIntrospectionEndpoint(tokenIntrospection -> customize(tokenIntrospection, httpSecurity));
    }

    public abstract void customize(OAuth2TokenIntrospectionEndpointConfigurer configurer, HttpSecurity http);
}
