package com.schilings.neiko.security.oauth2.authorization.server.customizer.token;

import com.schilings.neiko.security.oauth2.authorization.server.customizer.OAuth2TokenEndpointConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.password.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.password.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2ConfigurerUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientCredentialsAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2RefreshTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenEndpointConfigurer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * <p>
 * 放弃通过注入Bean到容器的打算，会破坏默认配置{@see InitializeAuthenticationProviderManagerConfigurer}
 * </p>
 * <p>
 * 例如，AuthenticationProvider的注入会破坏AuthenticationConfiguration的默认配置
 * </p>
 * <p>
 * 注入AuthenticationProvider不是一个很理智的想法
 * </p>
 *
 * @author Schilings
 */
@Order(Ordered.LOWEST_PRECEDENCE - 200)
public class DefaultOAuth2TokenEndpointConfigurerCustomizer extends OAuth2TokenEndpointConfigurerCustomizer {

	private OAuth2TokenResponseEnhancer oauth2TokenResponseEnhancer = OAuth2AccessTokenAuthenticationToken::getAdditionalParameters;

	private List<AuthenticationConverterExpander> converterExpanders;

	private List<AuthenticationProviderExpander> providerExpanders;

	private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter = new OAuth2AccessTokenResponseHttpMessageConverter();

	private final HttpMessageConverter<OAuth2Error> errorHttpResponseConverter = new OAuth2ErrorHttpMessageConverter();

	private AuthenticationSuccessHandler successHandler = this::sendAccessTokenResponse;

	private AuthenticationFailureHandler failureHandler = this::sendErrorResponse;

	private Function<AuthenticationSuccessHandler, AuthenticationSuccessHandler> successHandlerMapping = successHandler -> successHandler;

	private Function<AuthenticationFailureHandler, AuthenticationFailureHandler> failureHandlerMapping = failureHandler -> failureHandler;

	@Override
	public void customize(OAuth2TokenEndpointConfigurer configurer, HttpSecurity http) {
		// DelegatingOAuth2TokenGenerator
		OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator = OAuth2ConfigurerUtils.getTokenGenerator(http);
		OAuth2AuthorizationService authorizationService = OAuth2ConfigurerUtils.getAuthorizationService(http);
		ObjectPostProcessor<Object> postProcessor = http.getSharedObject(ObjectPostProcessor.class);
		// 未build,so 懒加载
		AuthenticationManager authenticationManager = authentication -> http
				.getSharedObject(AuthenticationManager.class).authenticate(authentication);
		// AuthenticationConverter
		List<AuthenticationConverter> converters = new ArrayList(Arrays.asList(
				new OAuth2AuthorizationCodeAuthenticationConverter(), new OAuth2RefreshTokenAuthenticationConverter(),
				new OAuth2ClientCredentialsAuthenticationConverter(),
				new OAuth2ResourceOwnerPasswordAuthenticationConverter()));

		// AuthenticationProvider
		List<AuthenticationProvider> providers = new ArrayList<>(
				Arrays.asList(new OAuth2AuthorizationCodeAuthenticationProvider(authorizationService, tokenGenerator),
						new OAuth2RefreshTokenAuthenticationProvider(authorizationService, tokenGenerator),
						new OAuth2ClientCredentialsAuthenticationProvider(authorizationService, tokenGenerator),
						new OAuth2ResourceOwnerPasswordAuthenticationProvider(authenticationManager,
								authorizationService, tokenGenerator)));

		extendAuthenticationConverter(converters, http);
		extendAuthenticationProvider(providers, authorizationService, tokenGenerator, http);

		DelegatingAuthenticationConverter authenticationConverter = new DelegatingAuthenticationConverter(converters);
		configurer.accessTokenRequestConverter(authenticationConverter);
		providers.forEach(configurer::authenticationProvider);

		this.successHandler = this.successHandlerMapping.apply(this.successHandler);
		this.failureHandler = this.failureHandlerMapping.apply(this.failureHandler);
		configurer.accessTokenResponseHandler(postProcessor.postProcess(this.successHandler));
		configurer.errorResponseHandler(postProcessor.postProcess(this.failureHandler));

	}

	private void extendAuthenticationConverter(List<AuthenticationConverter> converters, HttpSecurity http) {
		if (converterExpanders != null && !converterExpanders.isEmpty()) {
			for (AuthenticationConverterExpander expander : converterExpanders) {
				expander.expand(converters, http);
			}
		}
	}

	private void extendAuthenticationProvider(List<AuthenticationProvider> providers,
			OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
			HttpSecurity http) {
		if (providerExpanders != null && !providerExpanders.isEmpty()) {
			for (AuthenticationProviderExpander expander : providerExpanders) {
				expander.expand(providers, http);
			}
		}
	}

	public DefaultOAuth2TokenEndpointConfigurerCustomizer converterExpander(AuthenticationConverterExpander expander) {
		if (this.converterExpanders == null) {
			this.converterExpanders = new ArrayList<>();
		}
		this.converterExpanders.add(expander);
		return this;
	}

	public DefaultOAuth2TokenEndpointConfigurerCustomizer providerExpander(AuthenticationProviderExpander expander) {
		if (this.providerExpanders == null) {
			this.providerExpanders = new ArrayList<>();
		}
		this.providerExpanders.add(expander);
		return this;
	}

	public DefaultOAuth2TokenEndpointConfigurerCustomizer oauth2TokenResponseEnhancer(
			OAuth2TokenResponseEnhancer enhancer) {
		this.oauth2TokenResponseEnhancer = enhancer;
		return this;
	}

	public DefaultOAuth2TokenEndpointConfigurerCustomizer accessTokenResponseHandler(
			Function<AuthenticationSuccessHandler, AuthenticationSuccessHandler> apply) {
		this.successHandlerMapping = apply;
		return this;
	}

	public DefaultOAuth2TokenEndpointConfigurerCustomizer errorResponseHandler(
			Function<AuthenticationFailureHandler, AuthenticationFailureHandler> apply) {
		this.failureHandlerMapping = apply;
		return this;
	}

	private void sendAccessTokenResponse(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;

		OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
		OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();

		OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
				.tokenType(accessToken.getTokenType()).scopes(accessToken.getScopes());
		if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
			builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
		}
		if (refreshToken != null) {
			builder.refreshToken(refreshToken.getTokenValue());
		}
		// Token响应增强，不存储
		Map<String, Object> additionalParameters = oauth2TokenResponseEnhancer.enhance(accessTokenAuthentication);
		if (!CollectionUtils.isEmpty(additionalParameters)) {
			builder.additionalParameters(additionalParameters);
		}

		OAuth2AccessTokenResponse accessTokenResponse = builder.build();
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
		this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
	}

	private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException {

		OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
		httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);
		this.errorHttpResponseConverter.write(error, null, httpResponse);
	}

	@FunctionalInterface
	public interface AuthenticationConverterExpander {

		void expand(List<AuthenticationConverter> converters, HttpSecurity http);

	}

	@FunctionalInterface
	public interface AuthenticationProviderExpander {

		void expand(List<AuthenticationProvider> providers, HttpSecurity http);

	}

}
