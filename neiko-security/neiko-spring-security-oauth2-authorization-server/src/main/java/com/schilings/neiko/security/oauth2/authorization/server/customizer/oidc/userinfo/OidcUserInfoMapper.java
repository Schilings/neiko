package com.schilings.neiko.security.oauth2.authorization.server.customizer.oidc.userinfo;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;

import java.util.function.Function;

public interface OidcUserInfoMapper extends Function<OidcUserInfoAuthenticationContext, OidcUserInfo> {
    
}
