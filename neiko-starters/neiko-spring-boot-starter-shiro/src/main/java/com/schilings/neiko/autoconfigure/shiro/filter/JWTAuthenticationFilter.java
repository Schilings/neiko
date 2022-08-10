package com.schilings.neiko.autoconfigure.shiro.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.schilings.neiko.autoconfigure.shiro.exception.JWTAuthenticationFilterException;
import com.schilings.neiko.autoconfigure.shiro.token.JWTRepository;
import com.schilings.neiko.autoconfigure.shiro.token.JWTToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 过滤器执行流程<br>
 * {@link #preHandle(ServletRequest, ServletResponse) preHandle} ->
 * {@link #isLoginAttempt(ServletRequest, ServletResponse) isLoginAttempt} -> 
 * {@link #isAccessAllowed(ServletRequest, ServletResponse, Object) isAccessAllowed} -> 
 * 登录
 * <br>对于没有携带token的请求，中断拦截链，提示对于需要认证的请求未携带token
 * <br>对于有携带token的请求则尝试使用token进行认证
 * 
 * @author Ken-Chy129
 * @date 2022/8/4 14:14
 */
@AllArgsConstructor
@Slf4j
public class JWTAuthenticationFilter extends BasicHttpAuthenticationFilter {
    
    private static final String AUTHORIZATION_HEADER = "X-Authorization";
    
    private static final String AUTHORIZATION_PARAM = "token";
    
    private JWTRepository jwtRepository;

    /**
     * 通过 {@link #isLoginAttempt(ServletRequest, ServletResponse) isLoginAttempt} 判断是否有携带token，如果没有携带则请求失败，如果携带则验证token是否合法，如果合法则调用认证，如果token不合法或认证失败都中断请求
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            JWTToken token = new JWTToken(getToken(request));
            try {
                Subject subject = getSubject(request, response);
                if (ObjectUtil.isNull(jwtRepository)) {
                    String msg = "JWTRepository is null, please write a class to extend 'JWTRepository' and implement the functions and then set it for the filter so that it can complete the verification of the token.";
                    log.error(msg);
                }
                jwtRepository.verify(token.getToken());
                subject.login(token);
                return onLoginSuccess(token, subject, request, response);
            } catch (JWTVerificationException e) {
                log.error("token invalid : " + e.getMessage());
            } catch (AuthenticationException e) {
                log.error("authentication error : " + e.getMessage());
                return onLoginFailure(token, e, request, response);
            } catch (Exception e) {
                log.error("request error : " + e.getMessage());
            }
        }
        String msg = "Attempting to access a path which requires authentication. But no Java Web Token was found in the request whatever header, param or cookie.";
        log.error(msg);
        return false;
    }

    /**
     * 判断请求是否携带token
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        return getToken(request) != null;
    }

    /**
     * 跨域时会首先发送一个option请求，对于option请求设置请求成功并返回false以中断拦截链，反之返回true继续判断
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = WebUtils.toHttp(request);
        HttpServletResponse res = WebUtils.toHttp(response);
        res.setHeader("Access-control-Allow-Origin", req.getHeader("Origin"));
        res.setHeader("Access-control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        res.setHeader("Access-control-Allow-Headers", req.getHeader("Access-Control-Request-Headers"));
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            res.setStatus(HttpStatus.HTTP_OK);
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 先后尝试从请求头，请求参数和cookie中获取token，不存在则返回null
     */
    protected String getToken(ServletRequest request) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        String token = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isEmpty(token)) {
            token = httpServletRequest.getParameter(AUTHORIZATION_PARAM);
            if (StringUtils.isEmpty(token)) {
                Cookie[] cookies = httpServletRequest.getCookies();
                if (null == cookies || cookies.length == 0) {
                    return null;
                }
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(AUTHORIZATION_PARAM)) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }
        return token;
    }
    
}
