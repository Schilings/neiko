package com.schilings.neiko.security.oauth2.authorization.server.customizer.revocation;

import com.schilings.neiko.security.oauth2.authorization.server.customizer.OAuth2TokenRevocationEndpointConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2ConfigurerUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenRevocationEndpointConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Function;

public class DefaultOAuth2TokenRevocationCustomizer extends OAuth2TokenRevocationEndpointConfigurerCustomizer {

	private final HttpMessageConverter<OAuth2Error> errorHttpResponseConverter = new OAuth2ErrorHttpMessageConverter();

	private AuthenticationSuccessHandler successHandler = this::sendRevocationSuccessResponse;

	private AuthenticationFailureHandler failureHandler = this::sendErrorResponse;

	private Function<AuthenticationSuccessHandler, AuthenticationSuccessHandler> successHandlerMapping = (
			successHandler) -> successHandler;

	private Function<AuthenticationFailureHandler, AuthenticationFailureHandler> failureHandlerMapping = (
			failureHandler) -> failureHandler;

	@Override
	public void customize(OAuth2TokenRevocationEndpointConfigurer configurer, HttpSecurity http) {

		OAuth2AuthorizationService authorizationService = OAuth2ConfigurerUtils.getAuthorizationService(http);
		this.successHandler = this.successHandlerMapping.apply(this.successHandler);
		this.failureHandler = this.failureHandlerMapping.apply(this.failureHandler);
		configurer.authenticationProvider(new OAuth2TokenRevocationAuthenticationProvider(authorizationService))
				.revocationResponseHandler(this.successHandler).errorResponseHandler(this.failureHandler);
	}

	public DefaultOAuth2TokenRevocationCustomizer revocationResponseHandler(
			Function<AuthenticationSuccessHandler, AuthenticationSuccessHandler> apply) {
		this.successHandlerMapping = apply;
		return this;
	}

	public DefaultOAuth2TokenRevocationCustomizer errorResponseHandler(
			Function<AuthenticationFailureHandler, AuthenticationFailureHandler> apply) {
		this.failureHandlerMapping = apply;
		return this;
	}

	private void sendRevocationSuccessResponse(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		response.setStatus(HttpStatus.OK.value());
	};

	private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException {
		OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
		httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);
		this.errorHttpResponseConverter.write(error, null, httpResponse);
	}

}
