package com.schilings.neiko.security.oauth2.authorization.server.customizer.introspection;


import com.schilings.neiko.security.oauth2.authorization.server.customizer.OAuth2TokenIntrospectionEndpointConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.revocation.DefaultOAuth2TokenRevocationCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenIntrospection;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenIntrospectionAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenIntrospectionEndpointConfigurer;
import org.springframework.security.oauth2.server.authorization.http.converter.OAuth2TokenIntrospectionHttpMessageConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Function;

public class DefaultOAuth2TokenIntrospectionCustomizer extends OAuth2TokenIntrospectionEndpointConfigurerCustomizer {

    private final HttpMessageConverter<OAuth2TokenIntrospection> tokenIntrospectionHttpResponseConverter =
            new OAuth2TokenIntrospectionHttpMessageConverter();
    private final HttpMessageConverter<OAuth2Error> errorHttpResponseConverter = new OAuth2ErrorHttpMessageConverter();
    
    private AuthenticationSuccessHandler successHandler = this::sendIntrospectionResponse;
    private AuthenticationFailureHandler failureHandler = this::sendErrorResponse;

    private Function<AuthenticationSuccessHandler, AuthenticationSuccessHandler> successHandlerMapping = (
            successHandler) -> successHandler;
    private Function<AuthenticationFailureHandler, AuthenticationFailureHandler> failureHandlerMapping = (
            failureHandler) -> failureHandler;
    
    
    @Override
    public void customize(OAuth2TokenIntrospectionEndpointConfigurer configurer, HttpSecurity http) {
        this.successHandler = this.successHandlerMapping.apply(this.successHandler);
        this.failureHandler = this.failureHandlerMapping.apply(this.failureHandler);

        configurer
                .introspectionResponseHandler(successHandler)
                .errorResponseHandler(failureHandler);
    }


    public DefaultOAuth2TokenIntrospectionCustomizer revocationResponseHandler(
            Function<AuthenticationSuccessHandler, AuthenticationSuccessHandler> apply) {
        this.successHandlerMapping = apply;
        return this;
    }

    public DefaultOAuth2TokenIntrospectionCustomizer errorResponseHandler(
            Function<AuthenticationFailureHandler, AuthenticationFailureHandler> apply) {
        this.failureHandlerMapping = apply;
        return this;
    }
    

    private void sendIntrospectionResponse(HttpServletRequest request, HttpServletResponse response,
                                           Authentication authentication) throws IOException {

        OAuth2TokenIntrospectionAuthenticationToken tokenIntrospectionAuthentication =
                (OAuth2TokenIntrospectionAuthenticationToken) authentication;
        OAuth2TokenIntrospection tokenClaims = tokenIntrospectionAuthentication.getTokenClaims();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        this.tokenIntrospectionHttpResponseConverter.write(tokenClaims, null, httpResponse);
    }

    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response,
                                   AuthenticationException exception) throws IOException {
        OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);
        this.errorHttpResponseConverter.write(error, null, httpResponse);
    }
}
