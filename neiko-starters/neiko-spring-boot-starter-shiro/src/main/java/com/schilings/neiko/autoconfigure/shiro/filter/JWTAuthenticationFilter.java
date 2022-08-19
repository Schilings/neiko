package com.schilings.neiko.autoconfigure.shiro.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.schilings.neiko.autoconfigure.shiro.token.JWTRepository;
import com.schilings.neiko.autoconfigure.shiro.token.JWTToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 过滤器执行流程<br>
 * {@link #preHandle(ServletRequest, ServletResponse) preHandle} ->
 * {@link #isLoginAttempt(ServletRequest, ServletResponse) isLoginAttempt} ->
 * {@link #isAccessAllowed(ServletRequest, ServletResponse, Object) isAccessAllowed} ->
 * <br>
 * true: 使用subject.login进行登录，会调用realm中的认证方法(如果缓存中没有权限信息) <br>
 * false: {@link #onAccessDenied(ServletRequest, ServletResponse) onAccessDenied}
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
	 * 通过 {@link #isLoginAttempt(ServletRequest, ServletResponse) isLoginAttempt}
	 * 判断是否有携带token，如果携带则验证token是否合法，如果合法则调用认证，如果没有携带token或者token不合法、认证失败等都中断请求，执行{@link #onAccessDenied(ServletRequest, ServletResponse)
	 * onAccessDenied}
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		if (isLoginAttempt(request, response)) {
			JWTToken token = new JWTToken(getAuthzHeader(request));
			try {
				Subject subject = getSubject(request, response);
				if (ObjectUtil.isNull(jwtRepository)) {
					log.error(
							"DefaultJWTRepository is null, please write a class to extend 'DefaultJWTRepository' and implement the functions and then set it for the filter so that it can complete the verification of the token.");
					return false;
				}
				jwtRepository.verify(token.getToken());
				subject.login(token);
				return onLoginSuccess(token, subject, request, response);
			}
			catch (JWTVerificationException e) {
				log.error("token invalid : " + e.getMessage());
				return false;
			}
			catch (AuthenticationException e) {
				log.error("authentication error : " + e.getMessage());
				return false;
			}
			catch (Exception e) {
				log.error("request error : " + e.getMessage());
				return false;
			}
		}
		log.error(
				"Attempting to access '{}' which requires authentication. But no Java Web Token was found in the request whatever header, param or cookie.",
				((HttpServletRequest) request).getRequestURI());
		return false;
	}

	/**
	 * 判断请求是否携带token
	 */
	@Override
	protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
		return getAuthzHeader(request) != null;
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
	@Override
	protected String getAuthzHeader(ServletRequest request) {
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

	/**
	 * 重写请求被拒绝(认证失败)时的执行方法 中断后续过滤，直接返回
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		WebUtils.toHttp(response).setStatus(HttpStatus.HTTP_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		Map<String, Object> res = new HashMap<>();
		res.put("code", 401);
		res.put("msg", "Request failed for not passing authenticating");
		JSONObject.writeJSONString(response.getWriter(), res);
		return false;
	}

}
