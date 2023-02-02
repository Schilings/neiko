package com.schilings.neiko.authorization.biz.federated;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

@FunctionalInterface
public interface OidcUserService {

	OAuth2User loadUser(OidcUser oidcUser, String userNameAttributeName);

}
