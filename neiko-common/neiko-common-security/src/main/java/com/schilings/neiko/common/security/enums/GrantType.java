package com.schilings.neiko.common.security.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * <p>授权类：授权码 刷新令牌 密码 隐式 客户端凭证</p>
 * 
 * @author Schilings
*/
@AllArgsConstructor
@Getter
public enum GrantType {

    AuthorizationCode("authorization_code"),
    RefreshToken("refresh_token"),
    Password("password"),
    Implicit("implicit"),
    ClientCredentials("client_credentials"),
    
    //自定义
    Moblie("mobile"),
    EMAIl("email"),
    ;
    private String grant;
    
}
