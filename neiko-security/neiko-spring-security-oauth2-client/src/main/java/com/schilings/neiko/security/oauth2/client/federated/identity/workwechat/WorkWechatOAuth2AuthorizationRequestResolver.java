package com.schilings.neiko.security.oauth2.client.federated.identity.workwechat;

import com.schilings.neiko.security.oauth2.client.CommonOAuth2Provider;
import com.schilings.neiko.security.oauth2.client.federated.identity.wechat.WechatParameterNames;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class WorkWechatOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

	private static final String REGISTRATION_ID_URI_VARIABLE_NAME = "registrationId";

	public static Set<String> TARGET_CLIENT_REGISTRATION_IDS = new HashSet<>(
			Arrays.asList(CommonOAuth2Provider.WORK_WECHAT_WEB_QR.name().toLowerCase()));

	private final ClientRegistrationRepository clientRegistrationRepository;

	private final AntPathRequestMatcher authorizationRequestMatcher;

	private final DefaultOAuth2AuthorizationRequestResolver delegate;

	public WorkWechatOAuth2AuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository,
			String authorizationRequestBaseUri) {
		Assert.notNull(clientRegistrationRepository,
				"clientRegistrationRepository can not be null in HttpSecurity SharedObjects");
		this.delegate = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository,
				authorizationRequestBaseUri);
		this.delegate.setAuthorizationRequestCustomizer(this::customize);
		this.clientRegistrationRepository = clientRegistrationRepository;
		this.authorizationRequestMatcher = new AntPathRequestMatcher(
				authorizationRequestBaseUri + "/{" + REGISTRATION_ID_URI_VARIABLE_NAME + "}");

	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
		// 只处理Work Wechat
		String clientRegistrationId = resolveRegistrationId(request);
		if (clientRegistrationId == null) {
			return null;
		}
		// ClientRegistration clientRegistration =
		// clientRegistrationRepository.findByRegistrationId(clientRegistrationId);
		if (TARGET_CLIENT_REGISTRATION_IDS.contains(clientRegistrationId)) {
			return this.delegate.resolve(request);
		}
		return null;
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
		// 只处理WORK_WECHAT_WEB_QR
		if (clientRegistrationId == null) {
			return null;
		}
		if (!TARGET_CLIENT_REGISTRATION_IDS.contains(clientRegistrationId)) {
			return null;
		}
		return this.delegate.resolve(request, clientRegistrationId);
	}

	private String resolveRegistrationId(HttpServletRequest request) {
		if (this.authorizationRequestMatcher.matches(request)) {
			return this.authorizationRequestMatcher.matcher(request).getVariables()
					.get(REGISTRATION_ID_URI_VARIABLE_NAME);
		}
		return null;
	}

	/**
	 * 默认情况下Spring Security会生成授权链接：
	 * {@code https://open.work.weixin.qq.com/wwopen/sso/qrConnect?response_type=code
	 * &client_id=wxdf9033184b238e7f
	 * &scope=snsapi_userinfo
	 * &state=5NDiQTMa9ykk7SNQ5-OIJDbIy9RLaEVzv3mdlj8TjuE%3D
	 * &redirect_uri=https%3A%2F%2Fmov-h5-test.felord.cn} 企业微信协议：
	 * {@code client_id}应该替换为{@code app_id} {@code scope}借用scope替换为{@code agent_id}
	 * @param builder the OAuth2AuthorizationRequest.builder
	 */
	private void customize(OAuth2AuthorizationRequest.Builder builder) {
		builder.attributes(attributes -> builder.parameters(parameters -> {
			LinkedHashMap<String, Object> linkedParameters = new LinkedHashMap<>();
			parameters.forEach((k, v) -> {
				if (OAuth2ParameterNames.CLIENT_ID.equals(k)) {
					linkedParameters.put(WechatParameterNames.APP_ID, v);
				}
				if (OAuth2ParameterNames.REDIRECT_URI.equals(k)) {
					linkedParameters.put(OAuth2ParameterNames.REDIRECT_URI, v);
				}
				if (OAuth2ParameterNames.STATE.equals(k)) {
					linkedParameters.put(OAuth2ParameterNames.STATE, v);
				}
				// 借用scope
				if (OAuth2ParameterNames.SCOPE.equals(k)) {
					// 1000005
					linkedParameters.put(WechatParameterNames.AGENT_ID, v);
				}
			});
			parameters.clear();
			parameters.putAll(linkedParameters);
		}));

	}

}
