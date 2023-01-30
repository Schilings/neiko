package com.schilings.neiko.authorization.biz.federated;

import org.springframework.security.oauth2.core.user.OAuth2User;

@FunctionalInterface
public interface OAuth2UserService {

    OAuth2User loadUser(OAuth2User oAuth2User, String userNameAttributeName);
    
}
