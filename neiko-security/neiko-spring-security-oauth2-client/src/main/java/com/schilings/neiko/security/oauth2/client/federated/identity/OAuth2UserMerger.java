package com.schilings.neiko.security.oauth2.client.federated.identity;

import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.function.Consumer;

/**
 *
 * <p>
 * 信息融合
 * </p>
 *
 * @author Schilings
 */
public interface OAuth2UserMerger {

	OAuth2User merge(OAuth2LoginAuthenticationToken user);

}
