package com.schilings.neiko.auth.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(value = Ordered.LOWEST_PRECEDENCE - 10)
@RestControllerAdvice
@RequiredArgsConstructor
public class AuthHandlerExceptionResolver {

}
