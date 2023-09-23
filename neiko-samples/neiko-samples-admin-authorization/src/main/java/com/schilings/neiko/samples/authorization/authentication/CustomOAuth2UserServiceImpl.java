package com.schilings.neiko.samples.authorization.authentication;

import com.schilings.neiko.authorization.biz.federated.OAuth2UserService;
import com.schilings.neiko.common.util.web.WebUtils;
import com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2EndpointUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class CustomOAuth2UserServiceImpl implements OAuth2UserService {

	@Override
	public OAuth2User loadUser(OAuth2User oAuth2User, String userNameAttributeName) {
		return oAuth2User;
	}

}
