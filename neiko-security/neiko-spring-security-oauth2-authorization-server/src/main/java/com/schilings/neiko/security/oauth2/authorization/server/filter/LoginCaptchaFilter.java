package com.schilings.neiko.security.oauth2.authorization.server.filter;

import cn.hutool.core.text.CharSequenceUtil;

import com.schilings.neiko.security.captcha.CaptchaValidateResult;
import com.schilings.neiko.security.captcha.CaptchaValidator;
import com.schilings.neiko.security.oauth2.core.ScopeNames;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient;

@RequiredArgsConstructor
public class LoginCaptchaFilter extends OncePerRequestFilter {

	private static final String CAPTCHA_CODE_ERROR = "captcha_code_error";

	private final RequestMatcher requestMatcher;

	private final CaptchaValidator captchaValidator;

	private final OAuth2ErrorHttpMessageConverter errorHttpResponseConverter = new OAuth2ErrorHttpMessageConverter();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!this.requestMatcher.matches(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 只对 password 的 grant_type 进行拦截处理
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!AuthorizationGrantType.PASSWORD.getValue().equals(grantType)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 获取当前客户端
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(authentication);
		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

		// 测试客户端 跳过验证码（swagger 或 postman测试时使用）
		if (registeredClient != null && registeredClient.getScopes().contains(ScopeNames.SKIP_CAPTCHA)) {
			filterChain.doFilter(request, response);
			return;
		}

		CaptchaValidateResult captchaValidateResult = captchaValidator.validate(request);
		if (captchaValidateResult.isSuccess()) {
			filterChain.doFilter(request, response);
		}
		else {
			sendErrorResponse(request, response, captchaValidateResult);
		}

	}

	private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response,
			CaptchaValidateResult captchaValidateResult) throws IOException {

		OAuth2Error error = new OAuth2Error(CAPTCHA_CODE_ERROR, captchaValidateResult.getMessage(), "");
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
		httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
		this.errorHttpResponseConverter.write(error, null, httpResponse);

	}

}
