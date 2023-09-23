package com.schilings.neiko.authorization.biz.tokencustomizer;


import com.schilings.neiko.authorization.common.constant.TokenAttributeNameConstants;
import com.schilings.neiko.authorization.common.constant.UserInfoFiledNameConstants;
import com.schilings.neiko.authorization.common.userdetails.User;
import com.schilings.neiko.authorization.common.util.SecurityUtils;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.OAuth2TokenResponseEnhancer;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserInfoOAuth2TokenPesponseEnhancer implements OAuth2TokenResponseEnhancer {
    
    @Override
    public Map<String, Object> enhance(OAuth2AccessTokenAuthenticationToken accessTokenAuthentication) {
        return extractUser(accessTokenAuthentication);
    }

    private Map<String,Object> extractUser(OAuth2AccessTokenAuthenticationToken authentication) {
//        if (!authentication.getAccessToken().getScopes().contains("????")) {
//            return authentication.getAdditionalParameters();
//        }
        User user = SecurityUtils.getUser();
        if (user != null) {
            HashMap<String, Object> additionalParameters = new HashMap<>(authentication.getAdditionalParameters());
            Map<String, Object> info = Map.of(
                    UserInfoFiledNameConstants.USER_ID, user.getUserId(),
                    UserInfoFiledNameConstants.TYPE, user.getType(),
                    UserInfoFiledNameConstants.ORGANIZATION_ID, user.getOrganizationId(),
                    UserInfoFiledNameConstants.USERNAME, user.getUsername(),
                    UserInfoFiledNameConstants.NICKNAME, user.getNickname()
            );
            additionalParameters.put(TokenAttributeNameConstants.INFO, info);
            additionalParameters.put(TokenAttributeNameConstants.ATTRIBUTES, user.getAttributes());
            return additionalParameters;
        }
        return Collections.emptyMap();
    }
}
