package com.schilings.neiko.security.oauth2.authorization.server.filter;

import com.schilings.neiko.security.oauth2.core.ModifyParamMapRequestWrapper;
import com.schilings.neiko.security.oauth2.core.ScopeNames;
import com.schilings.neiko.security.util.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient;

/**
 *
 * <p>
 * 前端传递过来的加密密码，需要在登陆之前先解密
 * </p>
 *
 * @author Schilings
 */
@Slf4j
public class PasswordDecoderFilter extends OncePerRequestFilter {

	public static final String PASSWORD_DECODE_AES_ERROR = "password_decode_aes_error";

	private final RequestMatcher requestMatcher;

	private final String passwordSecretKey;

	private final OAuth2ErrorHttpMessageConverter errorHttpResponseConverter = new OAuth2ErrorHttpMessageConverter();

	public PasswordDecoderFilter(RequestMatcher requestMatcher, String passwordSecretKey) {
		this.requestMatcher = requestMatcher;
		this.passwordSecretKey = passwordSecretKey;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!this.requestMatcher.matches(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 未配置密码密钥时，直接跳过
		if (!StringUtils.hasText(passwordSecretKey)) {
			log.warn("passwordSecretKey not configured since empty, skip password decoder");
			filterChain.doFilter(request, response);
			return;
		}

		// 非密码模式下，直接跳过
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!AuthorizationGrantType.PASSWORD.getValue().equals(grantType)) {
			filterChain.doFilter(request, response);
			return;
		}
		// 解密前台加密后的密码
		String passwordAes = request.getParameter(OAuth2ParameterNames.PASSWORD);
		if (!StringUtils.hasText(passwordAes)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 获取当前客户端
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(authentication);
		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

		// 隐式跳过密码解密。即不需要scope中填入
		// 测试客户端密码不加密，直接跳过（swagger 或 postman测试时使用）
		if (registeredClient != null && registeredClient.getScopes().contains(ScopeNames.SKIP_PASSWORD_DECODE)) {
			filterChain.doFilter(request, response);
			return;
		}

		Map<String, String[]> parameterMap = new HashMap<>(request.getParameterMap());
		try {
			String password = PasswordUtils.decodeAES(passwordAes, passwordSecretKey);
			parameterMap.put(OAuth2ParameterNames.PASSWORD, new String[] { password });
		}
		catch (Exception e) {
			log.error("[doFilterInternal] password decode aes error，passwordAes: {}，passwordSecretKey: {}", passwordAes,
					passwordSecretKey, e);
			sendErrorResponse(request, response, e);
			return;
		}

		// SpringSecurity 默认从ParameterMap中获取密码参数
		// 由于原生的request中对parameter加锁了，无法修改，所以使用包装类
		filterChain.doFilter(new ModifyParamMapRequestWrapper(request, parameterMap), response);
	}

	private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception exception)
			throws IOException {

		OAuth2Error error = new OAuth2Error(PASSWORD_DECODE_AES_ERROR);
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
		httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);
		this.errorHttpResponseConverter.write(error, null, httpResponse);
	}

}
