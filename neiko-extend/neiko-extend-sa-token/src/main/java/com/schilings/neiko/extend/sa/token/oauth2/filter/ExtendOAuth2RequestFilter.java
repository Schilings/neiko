package com.schilings.neiko.extend.sa.token.oauth2.filter;

import cn.hutool.http.ContentType;
import com.schilings.neiko.common.core.request.wrapper.RepeatBodyRequestWrapper;
import com.schilings.neiko.common.core.request.wrapper.RepeatJsonBodyRequestWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * <p>
 * 控制Oauth请求，且封装重复可读Body
 * </p>
 *
 * @author Schilings
 */
@Slf4j
@RequiredArgsConstructor
public class ExtendOAuth2RequestFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 排除logout
		if (request.getRequestURI().matches("/oauth2/logout")) {
			filterChain.doFilter(request, response);
			return;
		}

		RepeatJsonBodyRequestWrapper requestWrapper = null;
		if (request instanceof RepeatJsonBodyRequestWrapper) {
			requestWrapper = (RepeatJsonBodyRequestWrapper) request;
		}
		if (request instanceof RepeatBodyRequestWrapper) {
			requestWrapper = new RepeatJsonBodyRequestWrapper((RepeatBodyRequestWrapper) request);
		}
		if (requestWrapper == null && request.getMethod().equals(HttpMethod.POST.name())
				&& request.getContentType().equals(ContentType.JSON.getValue())) {
			requestWrapper = new RepeatJsonBodyRequestWrapper(request);
		}
		Assert.notNull(requestWrapper, "登录认证请求不合法");
		filterChain.doFilter(requestWrapper, response);
	}

}
