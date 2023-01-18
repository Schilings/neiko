package com.schilings.neiko.authorization.common.util;


import com.schilings.neiko.authorization.common.userdetails.ClientPrincipal;
import com.schilings.neiko.authorization.common.userdetails.User;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;


@UtilityClass
public class SecurityUtils {

	/**
	 * 获取Authentication
	 */

	public Authentication getAuthentication() {
		return  SecurityContextHolder.getContext().getAuthentication();
	}
	


	/**
	 * 获取系统用户Details
	 * @param authentication 令牌
	 * @return User
	 * <p>
	 */
	public User getUser(Authentication authentication) {
		if (authentication == null) {
			return null;
		}
		if (authentication instanceof BearerTokenAuthentication) {
			//bearerTokenAuthentication.getPrincipal() 是 OAuth2IntrospectionAuthenticatedPrincipal
			BearerTokenAuthentication bearerTokenAuthentication = (BearerTokenAuthentication) authentication;
			Object userDetails = bearerTokenAuthentication.getTokenAttributes().get(UserDetails.class.getName());
			if (userDetails != null && userDetails instanceof User) {
				return (User) userDetails;
			}
			Object oauth2User = bearerTokenAuthentication.getTokenAttributes().get(OAuth2User.class.getName());
			if (oauth2User != null && oauth2User instanceof User) {
				return (User) oauth2User;
			}
		} else if (authentication.getPrincipal() instanceof User) {
			return (User) authentication.getPrincipal();
		}
		return null;
	}

	/**
	 * 获取用户详情
	 */
	public User getUser() {
		Authentication authentication = getAuthentication();
		return getUser(authentication);
	}

	/**
	 * 获取客户端信息
	 */
	public ClientPrincipal getClientPrincipal() {
		Authentication authentication = getAuthentication();
		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof ClientPrincipal) {
			return (ClientPrincipal) principal;
		}
		return null;
	}

}
