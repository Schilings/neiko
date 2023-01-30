package com.schilings.neiko.authorization.biz.federated;

import com.schilings.neiko.security.oauth2.client.federated.identity.wechat.WechatOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@FunctionalInterface
public interface WechatUserService {

    OAuth2User loadUser(WechatOAuth2User user, String userNameAttributeName);
    
}
