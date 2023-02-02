package com.schilings.neiko.authorization.biz.federated;

import com.schilings.neiko.security.oauth2.client.federated.identity.workwechat.WorkWechatOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@FunctionalInterface
public interface WorkWechatUserService {

	OAuth2User loadUser(WorkWechatOAuth2User workWechatOAuth2User, String userNameAttributeName);

}
