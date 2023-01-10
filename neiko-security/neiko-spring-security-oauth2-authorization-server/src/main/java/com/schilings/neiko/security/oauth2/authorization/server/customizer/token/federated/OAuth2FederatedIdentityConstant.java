package com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;

public final class OAuth2FederatedIdentityConstant {

    public static final String RESPONSE_TYPE = OAuth2ParameterNames.RESPONSE_TYPE;
    public static final String REGISTRATION_ID = OAuth2ParameterNames.REGISTRATION_ID;
    public static final String REDIRECT_URI = OAuth2ParameterNames.REDIRECT_URI;
    public static final String CODE = OAuth2ParameterNames.CODE;
    public static final String EXPIRES_IN = OAuth2ParameterNames.EXPIRES_IN;
    public static final String CLIENT_ID = OAuth2ParameterNames.CLIENT_ID;
    
    public static final String FEDERATED_IDENTITY_AUTHORIZATION_REQUEST = "federated_identity";

    /**
     * 自定义的令牌类型，用来校验是否登录成功
     */
    public static final String FEDERATED_IDENTITY_AUTHENTICATED_CODE = "federated_identity_code";
    public static final OAuth2TokenType FEDERATED_IDENTITY_AUTHENTICATED_CODE_TOKEN_TYPE =
            new OAuth2TokenType(FEDERATED_IDENTITY_AUTHENTICATED_CODE);

    /**
     * 自定义的OAuth授权方式
     */
    public static final AuthorizationGrantType FEDERATED_IDENTITY = new AuthorizationGrantType("federated_identity");

}
