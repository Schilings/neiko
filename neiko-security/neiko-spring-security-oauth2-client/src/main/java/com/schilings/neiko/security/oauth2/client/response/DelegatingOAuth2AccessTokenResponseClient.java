package com.schilings.neiko.security.oauth2.client.response;


import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class DelegatingOAuth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    private final OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> defaultAccessTokenResponseClient;

    private final List<OAuth2AccessTokenResponseClient> accessTokenResponseClients;
    
    public DelegatingOAuth2AccessTokenResponseClient(List<OAuth2AccessTokenResponseClient> accessTokenResponseClients,
                                                     OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> defaultAccessTokenResponseClient) {
        this.defaultAccessTokenResponseClient = defaultAccessTokenResponseClient;
        Assert.notNull(accessTokenResponseClients,"accessTokenResponseClients can not be null.");
        this.accessTokenResponseClients = Collections.unmodifiableList(accessTokenResponseClients);
        
    }

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
        if (!CollectionUtils.isEmpty(accessTokenResponseClients)) {
            // @formatter:off
            if (!CollectionUtils.isEmpty(this.accessTokenResponseClients)) {
                for (OAuth2AccessTokenResponseClient client : this.accessTokenResponseClients) {
                    OAuth2AccessTokenResponse response = client.getTokenResponse(authorizationGrantRequest);
                    if (Objects.nonNull(response)) {
                        return response;
                    }
                }
            }
        }
        return defaultAccessTokenResponseClient.getTokenResponse(authorizationGrantRequest);
    }
    
}
