package com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.client;

import cn.hutool.core.util.BooleanUtil;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityCode;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityCodeGenerator;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityConstant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OAuth2FederatedIdentityAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final Log logger = LogFactory.getLog(getClass());

	private final OAuth2AuthorizationService authorizationService;

	private final RegisteredClientRepository registeredClientRepository;

	private OAuth2TokenGenerator<OAuth2FederatedIdentityCode> federatedIdentityCodeGenerator = new OAuth2FederatedIdentityCodeGenerator();

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	private OAuth2FederatedIdentityOAuth2AuthorizationRequestRepository authorizationRequestRepository = new OAuth2FederatedIdentityOAuth2AuthorizationRequestRepository();

	public OAuth2FederatedIdentityAuthenticationSuccessHandler(OAuth2AuthorizationService authorizationService,
			RegisteredClientRepository registeredClientRepository) {
		this.authorizationService = authorizationService;
		this.registeredClientRepository = registeredClientRepository;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;

		// 在OAuth2LoginAuthenticationFilter本该删除的，被我们留在这里，所以在这里删除
		OAuth2AuthorizationRequest authorizationRequest = this.authorizationRequestRepository
				.removeAuthorizationRequestInTheEnd(request, response);
		// 是Federated Request Authorization Request
		if (authorizationRequest == null
				|| !StringUtils.hasText(
						authorizationRequest.getAttribute(OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY_REQUEST))
				|| !BooleanUtil.toBoolean(authorizationRequest
						.getAttribute(OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY_REQUEST))) {
			OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST,
					"The OAuth2 Federated Request Authorization Request failed to check the "
							+ OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY_REQUEST,
					"");
			throw new OAuth2AuthenticationException(error);
		}

		// RegisteredClient，获取Token时再检验RegisteredClient
		RegisteredClient registeredClient = this.registeredClientRepository
				.findByClientId(authorizationRequest.getAttribute(OAuth2FederatedIdentityConstant.CLIENT_ID));
		if (registeredClient == null) {
			OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST,
					"The OAuth2 Federated Request Authorization Request failed to check the "
							+ OAuth2FederatedIdentityConstant.CLIENT_ID,
					"");
			throw new OAuth2AuthenticationException(error);
		}

		// redirect_uri,检验RegisteredClient是否有该redirect_uri
		if (!registeredClient.getRedirectUris()
				.contains((String) authorizationRequest.getAttribute(OAuth2FederatedIdentityConstant.REDIRECT_URI))) {
			OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_REDIRECT_URI, "The redirect_uri is invalid.",
					"");
			throw new OAuth2AuthenticationException(error);
		}

		// 生成Code 需要RegisteredClient..Token有效时长等等
		OAuth2TokenContext tokenContext = createTokenContext(registeredClient, authenticationToken);
		OAuth2FederatedIdentityCode federatedIdentityCode = this.federatedIdentityCodeGenerator.generate(tokenContext);
		if (federatedIdentityCode == null) {
			OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
					"The token generator failed to generate the federatedIdentity code.", "");
			throw new OAuth2AuthenticationException(error);
		}

		if (this.logger.isTraceEnabled()) {
			this.logger.trace("Generated Federate Identity Authorization code");
		}

		// 生成授权信息需要RegisteredClient
		OAuth2Authorization authorization = authorizationBuilder(registeredClient, authenticationToken,
				authorizationRequest)
						// 这个的目的看OAuth2FederatedIdentityAuthenticationProvider注释
						.attribute(OAuth2ParameterNames.STATE, federatedIdentityCode.getTokenValue())
						.token(federatedIdentityCode).build();
		this.authorizationService.save(authorization);

		if (this.logger.isTraceEnabled()) {
			this.logger.trace("Federated Indentity 授权模式 authorization saved");
		}

		// 不应该返回json字符串，应该前端调用接口，而后端重定向到了其他路径,前端拿不到响应体，登录成功后转发会前端页面
		// 大概只能前端url拼接参数？
		// sendJsonResponse(request, response, federatedIdentityCode);
		sendFederatedIdentityResponse(request, response, federatedIdentityCode, authorizationRequest);

	}

	private static OAuth2Authorization.Builder authorizationBuilder(RegisteredClient devClient,
			OAuth2AuthenticationToken authenticationToken, OAuth2AuthorizationRequest authorizationRequest) {
		// 获取Token时再检验RegisteredClient
		return OAuth2Authorization.withRegisteredClient(devClient)
				.authorizationGrantType(OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY)
				// OAuth2User的Name
				.principalName(authenticationToken.getPrincipal().getName())
				// 把OAuth2AuthenticationTokenn携带过去
				.attribute(Principal.class.getName(), authenticationToken)
				.attribute(OAuth2User.class.getName(), authenticationToken.getPrincipal())
				// 需要在OAuth2FederatedIdentityAuthenticationProvider校验是否clientId与获取Token的clientId一致
				.attribute(OAuth2FederatedIdentityConstant.CLIENT_ID, devClient.getClientId())
				// 放入registrationId
				.attribute(OAuth2FederatedIdentityConstant.REGISTRATION_ID,
						authenticationToken.getAuthorizedClientRegistrationId())
				// 放入redirect_uri
				.attribute(OAuth2FederatedIdentityConstant.REDIRECT_URI,
						authorizationRequest.getAttribute(OAuth2FederatedIdentityConstant.REDIRECT_URI));
	}

	private static OAuth2TokenContext createTokenContext(RegisteredClient devClient,
			OAuth2AuthenticationToken authenticationToken) {
		// @formatter:off
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                ////登录先给假的的RegisteredClient，获取Token时再检验RegisteredClient
                .registeredClient(devClient)
                //OAuth2AuthenticationToken
                .principal(authenticationToken)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .tokenType(OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY_CODE_TOKEN_TYPE)
                .authorizationGrantType(OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY)
                //OAuth2AuthenticationToken
                .authorizationGrant(authenticationToken);
        // @formatter:on
		return tokenContextBuilder.build();
	}

	private void sendJsonResponse(HttpServletRequest request, HttpServletResponse response,
			OAuth2FederatedIdentityCode code) throws IOException {
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
		new OAuth2FederatedIdentityCodeHttpMessageConverter().write(code, null, httpResponse);
	}

	private void sendFederatedIdentityResponse(HttpServletRequest request, HttpServletResponse response,
			OAuth2FederatedIdentityCode federatedIdentityCode, OAuth2AuthorizationRequest authorizationRequest)
			throws IOException {
		// 携带参数转发到redirect_url
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromUriString(authorizationRequest.getAttribute(OAuth2FederatedIdentityConstant.REDIRECT_URI))
				.queryParam(OAuth2FederatedIdentityConstant.CODE, federatedIdentityCode.getTokenValue());
		String redirectUri;
		if (Objects.nonNull(federatedIdentityCode.getExpiresAt())) {
			uriBuilder.queryParam(OAuth2FederatedIdentityConstant.EXPIRES_IN, "{expires_in}");
			Map<String, String> queryParams = new HashMap<>();
			queryParams.put(OAuth2FederatedIdentityConstant.EXPIRES_IN,
					String.valueOf(getExpiresIn(federatedIdentityCode)));
			redirectUri = uriBuilder.build(queryParams).toString();
		}
		else {
			redirectUri = uriBuilder.toUriString();
		}
		this.redirectStrategy.sendRedirect(request, response, redirectUri);
	}

	private static long getExpiresIn(OAuth2FederatedIdentityCode federatedIdentityCode) {
		if (federatedIdentityCode.getExpiresAt() != null) {
			return ChronoUnit.SECONDS.between(Instant.now(), federatedIdentityCode.getExpiresAt());
		}
		return -1;
	}

}
