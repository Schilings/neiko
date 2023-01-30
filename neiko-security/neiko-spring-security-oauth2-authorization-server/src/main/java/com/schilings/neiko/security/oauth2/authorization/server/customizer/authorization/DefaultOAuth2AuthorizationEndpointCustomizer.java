package com.schilings.neiko.security.oauth2.authorization.server.customizer.authorization;

import com.schilings.neiko.security.oauth2.authorization.server.component.OAuth2SecurityContextRepository;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.OAuth2AuthorizationEndpointConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2ConfigurerUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationEndpointConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class DefaultOAuth2AuthorizationEndpointCustomizer extends OAuth2AuthorizationEndpointConfigurerCustomizer {

	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	private AuthenticationSuccessHandler authenticationSuccessHandler = this::sendAuthorizationResponse;

	private AuthenticationFailureHandler authenticationFailureHandler = this::sendErrorResponse;

	private Function<AuthenticationSuccessHandler, AuthenticationSuccessHandler> successHandlerMapping = (
			successHandler) -> successHandler;

	private Function<AuthenticationFailureHandler, AuthenticationFailureHandler> failureHandlerMapping = (
			failureHandler) -> failureHandler;
	
	private String consentPage;
	
	private boolean stateless = false;
	

	@Override
	public void customize(OAuth2AuthorizationServerConfigurer configurer, HttpSecurity httpSecurity) throws Exception {
		ObjectPostProcessor<Object> postProcessor = httpSecurity.getSharedObject(ObjectPostProcessor.class);
		// 使用无状态登录时，需要配合自定义的 SecurityContextRepository
		if (this.stateless) {
			httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			httpSecurity
					.securityContext(security -> security.securityContextRepository(
							new OAuth2SecurityContextRepository(OAuth2ConfigurerUtils.getAuthorizationService(httpSecurity))));
		}
		configurer.authorizationEndpoint(authorizationEndpoint -> {
			// custom consent 设置 OAuth2 Consent 地址
			if (StringUtils.hasText(this.consentPage)) {
				// 不然consent路径得不到认证，因为不经过Chain
				httpSecurity.securityMatchers().requestMatchers(new AntPathRequestMatcher(this.consentPage));
				authorizationEndpoint.consentPage(this.consentPage);
			}
			// handler
			this.authenticationSuccessHandler = this.successHandlerMapping.apply(this.authenticationSuccessHandler);
			this.authenticationFailureHandler = this.failureHandlerMapping.apply(this.authenticationFailureHandler);
			authorizationEndpoint.authorizationResponseHandler(postProcessor.postProcess(this.authenticationSuccessHandler));
			authorizationEndpoint.errorResponseHandler(postProcessor.postProcess(this.authenticationFailureHandler));
		});
	}

	public DefaultOAuth2AuthorizationEndpointCustomizer consentPage(String consentPage) {
		this.consentPage = consentPage;
		return this;
	}

	public DefaultOAuth2AuthorizationEndpointCustomizer stateless(boolean stateless) {
		this.stateless = stateless;
		return this;
	}
	

	public DefaultOAuth2AuthorizationEndpointCustomizer authorizationResponseHandler(
			Function<AuthenticationSuccessHandler, AuthenticationSuccessHandler> apply) {
		this.successHandlerMapping = apply;
		return this;
	}

	public DefaultOAuth2AuthorizationEndpointCustomizer errorResponseHandler(
			Function<AuthenticationFailureHandler, AuthenticationFailureHandler> apply) {
		this.failureHandlerMapping = apply;
		return this;
	}

	private void sendAuthorizationResponse(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication = (OAuth2AuthorizationCodeRequestAuthenticationToken) authentication;
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromUriString(authorizationCodeRequestAuthentication.getRedirectUri())
				.queryParam(OAuth2ParameterNames.CODE,
						authorizationCodeRequestAuthentication.getAuthorizationCode().getTokenValue());
		String redirectUri;
		if (StringUtils.hasText(authorizationCodeRequestAuthentication.getState())) {
			uriBuilder.queryParam(OAuth2ParameterNames.STATE, "{state}");
			Map<String, String> queryParams = new HashMap<>();
			queryParams.put(OAuth2ParameterNames.STATE, authorizationCodeRequestAuthentication.getState());
			redirectUri = uriBuilder.build(queryParams).toString();
		}
		else {
			redirectUri = uriBuilder.toUriString();
		}
		this.redirectStrategy.sendRedirect(request, response, redirectUri);
	}

	private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException {

		OAuth2AuthorizationCodeRequestAuthenticationException authorizationCodeRequestAuthenticationException = (OAuth2AuthorizationCodeRequestAuthenticationException) exception;
		OAuth2Error error = authorizationCodeRequestAuthenticationException.getError();
		OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication = authorizationCodeRequestAuthenticationException
				.getAuthorizationCodeRequestAuthentication();

		if (authorizationCodeRequestAuthentication == null
				|| !StringUtils.hasText(authorizationCodeRequestAuthentication.getRedirectUri())) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), error.toString());
			return;
		}

		if (this.logger.isTraceEnabled()) {
			this.logger.trace("Redirecting to client with error");
		}

		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromUriString(authorizationCodeRequestAuthentication.getRedirectUri())
				.queryParam(OAuth2ParameterNames.ERROR, error.getErrorCode());
		if (StringUtils.hasText(error.getDescription())) {
			uriBuilder.queryParam(OAuth2ParameterNames.ERROR_DESCRIPTION, error.getDescription());
		}
		if (StringUtils.hasText(error.getUri())) {
			uriBuilder.queryParam(OAuth2ParameterNames.ERROR_URI, error.getUri());
		}
		String redirectUri;
		if (StringUtils.hasText(authorizationCodeRequestAuthentication.getState())) {
			uriBuilder.queryParam(OAuth2ParameterNames.STATE, "{state}");
			Map<String, String> queryParams = new HashMap<>();
			queryParams.put(OAuth2ParameterNames.STATE, authorizationCodeRequestAuthentication.getState());
			redirectUri = uriBuilder.build(queryParams).toString();
		}
		else {
			redirectUri = uriBuilder.toUriString();
		}
		this.redirectStrategy.sendRedirect(request, response, redirectUri);
	}

}
