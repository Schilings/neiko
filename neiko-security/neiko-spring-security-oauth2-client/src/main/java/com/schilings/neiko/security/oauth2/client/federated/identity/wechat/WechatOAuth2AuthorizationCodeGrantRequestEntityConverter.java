package com.schilings.neiko.security.oauth2.client.federated.identity.wechat;


import com.schilings.neiko.security.oauth2.client.CommonOAuth2Provider;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class WechatOAuth2AuthorizationCodeGrantRequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

    private final OAuth2AuthorizationCodeGrantRequestEntityConverter delegate = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
    
    @Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
        String registrationId = authorizationCodeGrantRequest.getClientRegistration().getRegistrationId();
        //wechat_web_mp 、 wechat_web_qr
        if (registrationId.equals(CommonOAuth2Provider.WECHAT_WEB_MP.name().toLowerCase())) {
            return webchatWebMp().convert(authorizationCodeGrantRequest);
        } else if (registrationId.equals(CommonOAuth2Provider.WECHAT_WEB_QR.name().toLowerCase())) {
            return webchatWebQr().convert(authorizationCodeGrantRequest);
        }
        return delegate.convert(authorizationCodeGrantRequest);
    }
    
    private String encodeClientCredential(String clientCredential) {
        try {
            return URLEncoder.encode(clientCredential, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            // Will not happen since UTF-8 is a standard charset
            throw new IllegalArgumentException(ex);
        }
    }

    private HttpHeaders getTokenRequestHeaders(ClientRegistration clientRegistration) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")));
        final MediaType contentType = MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
        headers.setContentType(contentType);
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.equals(clientRegistration.getClientAuthenticationMethod())
                || ClientAuthenticationMethod.BASIC.equals(clientRegistration.getClientAuthenticationMethod())) {
            String clientId = encodeClientCredential(clientRegistration.getClientId());
            String clientSecret = encodeClientCredential(clientRegistration.getClientSecret());
            headers.setBasicAuth(clientId, clientSecret);
        }
        return headers;
    }
    

    /**
     * 微信公众号网页授权
     * @return
     */
    private Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> webchatWebMp() {
        return authorizationCodeGrantRequest -> {
            ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();
            HttpHeaders headers = getTokenRequestHeaders(clientRegistration);

            OAuth2AuthorizationExchange authorizationExchange = authorizationCodeGrantRequest.getAuthorizationExchange();
            MultiValueMap<String, String> queryParameters = new LinkedMultiValueMap<>();
            // grant_type
            queryParameters.add(OAuth2ParameterNames.GRANT_TYPE, authorizationCodeGrantRequest.getGrantType().getValue());
            // code
            queryParameters.add(OAuth2ParameterNames.CODE, authorizationExchange.getAuthorizationResponse().getCode());
            //appid
            queryParameters.add(WechatParameterNames.APP_ID, clientRegistration.getClientId());
            //secret
            queryParameters.add(WechatParameterNames.SECRET, clientRegistration.getClientSecret());
            String tokenUri = clientRegistration.getProviderDetails().getTokenUri();
            URI uri = UriComponentsBuilder.fromUriString(tokenUri).queryParams(queryParameters).build().toUri();
            return RequestEntity.get(uri).headers(headers).build();
        };
    }

    /**
     * 微信网页扫码Scan授权.
     * @return
     */
    private Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> webchatWebQr() {
        return authorizationCodeGrantRequest -> {
            ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();
            HttpHeaders headers = getTokenRequestHeaders(clientRegistration);

            OAuth2AuthorizationExchange authorizationExchange = authorizationCodeGrantRequest.getAuthorizationExchange();
            MultiValueMap<String, String> queryParameters = new LinkedMultiValueMap<>();
            // grant_type
            queryParameters.add(OAuth2ParameterNames.GRANT_TYPE, authorizationCodeGrantRequest.getGrantType().getValue());
            // code
            queryParameters.add(OAuth2ParameterNames.CODE, authorizationExchange.getAuthorizationResponse().getCode());
            // 如果有redirect-uri
            String redirectUri = authorizationExchange.getAuthorizationRequest().getRedirectUri();
            if (redirectUri != null) {
                queryParameters.add(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
            }
            //appid
            queryParameters.add(WechatParameterNames.APP_ID, clientRegistration.getClientId());
            //secret
            queryParameters.add(WechatParameterNames.SECRET, clientRegistration.getClientSecret());

            String tokenUri = clientRegistration.getProviderDetails().getTokenUri();

            URI uri = UriComponentsBuilder.fromUriString(tokenUri).queryParams(queryParameters).build().toUri();
            return RequestEntity.get(uri).headers(headers).build();
        };
    }
    
}
