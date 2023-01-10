package com.schilings.neiko.security.oauth2.client.federated.identity.workwechat;


import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;


public class WorkWechatOAuth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
    
    private final OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> delegate;
    
    public WorkWechatOAuth2AccessTokenResponseClient() {
        //ResponseClient
        DefaultAuthorizationCodeTokenResponseClient tokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
        // 微信返回的content-type 是 text-plain + 兼容微信解析
        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
        tokenResponseHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, new MediaType("application", "*+json")));
        tokenResponseHttpMessageConverter.setAccessTokenResponseConverter(new WorkWechatOAuth2AccessTokenResponseConverter());
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        
        tokenResponseClient.setRestOperations(restTemplate);
        tokenResponseClient.setRequestEntityConverter(new WorkWechatOAuth2AuthorizationCodeGrantRequestEntityConverter());
        
        this.delegate = tokenResponseClient;
    }

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
        //只处理WORK_WECHAT_WEB_QR
        String registrationId = authorizationGrantRequest.getClientRegistration().getRegistrationId();
        if (!WorkWechatOAuth2AuthorizationRequestResolver.TARGET_CLIENT_REGISTRATION_IDS.contains(registrationId)) {
            return null;
        }

        //TODO 缓存获取token 如果获取不到再请求 并放入缓存  企业微信的token不允许频繁获取
        OAuth2AccessTokenResponse tokenResponse = delegate.getTokenResponse(authorizationGrantRequest);
        String code = authorizationGrantRequest.getAuthorizationExchange()
                .getAuthorizationResponse()
                .getCode();
        return OAuth2AccessTokenResponse.withResponse(tokenResponse)
                .additionalParameters(Collections.singletonMap(OAuth2ParameterNames.CODE, code))
                .build();
    }
    
}
