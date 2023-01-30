package com.schilings.neiko.samples.authorization.authentication;


import com.schilings.neiko.authorization.biz.federated.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2UserServiceImpl implements OAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2User oAuth2User, String userNameAttributeName) {
        return oAuth2User;
    }
}
