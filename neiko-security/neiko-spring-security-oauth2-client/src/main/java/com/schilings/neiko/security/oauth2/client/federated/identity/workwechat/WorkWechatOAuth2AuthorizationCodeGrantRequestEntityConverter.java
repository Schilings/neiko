package com.schilings.neiko.security.oauth2.client.federated.identity.workwechat;

import com.schilings.neiko.security.oauth2.client.CommonOAuth2Provider;
import com.schilings.neiko.security.oauth2.client.federated.identity.wechat.WechatParameterNames;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class WorkWechatOAuth2AuthorizationCodeGrantRequestEntityConverter
		implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

	private final OAuth2AuthorizationCodeGrantRequestEntityConverter delegate = new OAuth2AuthorizationCodeGrantRequestEntityConverter();

	@Override
	public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
		String registrationId = authorizationCodeGrantRequest.getClientRegistration().getRegistrationId();
		// WORK_WECHAT_WEB_QR
		if (registrationId.equals(CommonOAuth2Provider.WORK_WECHAT_WEB_QR.name().toLowerCase())) {
			return workWebchatWebQr().convert(authorizationCodeGrantRequest);
		}
		return delegate.convert(authorizationCodeGrantRequest);
	}

	/**
	 * 企业微信网页扫码授权.
	 * https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ID&corpsecret=SECRET
	 */
	private Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> workWebchatWebQr() {
		return authorizationCodeGrantRequest -> {
			String code = authorizationCodeGrantRequest.getAuthorizationExchange().getAuthorizationResponse().getCode();

			if (code == null) {
				throw new OAuth2AuthenticationException("用户终止授权");
			}

			ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();

			MultiValueMap<String, String> queryParameters = new LinkedMultiValueMap<>();
			queryParameters.add(WechatParameterNames.CORP_ID, clientRegistration.getClientId());
			queryParameters.add(WechatParameterNames.CORP_SECRET, clientRegistration.getClientSecret());
			String tokenUri = clientRegistration.getProviderDetails().getTokenUri();
			URI uri = UriComponentsBuilder.fromUriString(tokenUri).queryParams(queryParameters).build().toUri();
			return RequestEntity.get(uri).build();
		};
	}

}
