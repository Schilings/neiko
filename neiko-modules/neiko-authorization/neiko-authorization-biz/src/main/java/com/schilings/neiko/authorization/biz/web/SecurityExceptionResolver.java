package com.schilings.neiko.authorization.biz.web;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 10)
@RestControllerAdvice
@RequiredArgsConstructor
public class SecurityExceptionResolver {

    private final AccessDeniedHandler accessDeniedHandler;

    /**
     * 处理拒绝访问异常,兼容spring webmvc的处理
     *
     * @param exception
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handlerAccessDeniedException(AccessDeniedException exception, HttpServletRequest request,
                                             HttpServletResponse response) throws ServletException, IOException {
        accessDeniedHandler.handle(request, response, exception);
    }

}
