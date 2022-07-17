package com.schilings.neiko.common.util.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;


/**
 * 用于Spring Web的客户端工具类
 */
@Slf4j
public class WebUtils extends org.springframework.web.util.WebUtils {
    /**
     * 获取当前ServletRequestAttributes
     * @return
     */
    public ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
    }

    /**
     * 获取当前HttpServletRequest
     * @return
     */
    public HttpServletRequest getRequest() {
        return Objects.requireNonNull(getServletRequestAttributes().getRequest());
    }

    /**
     * 获取当前HttpServletResponse
     * @return
     */
    public HttpServletResponse getResponse() {
        return Objects.requireNonNull(getServletRequestAttributes().getResponse());
    }
}
