package com.schilings.neiko.sample.oauth2.web;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    
    @ExceptionHandler(value = OAuth2AuthenticationException.class)
    public OAuth2AuthenticationException exception(OAuth2AuthenticationException exception) {
        return exception;
    }
}
