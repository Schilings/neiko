package com.schilings.neiko.authorization.common.util;

import com.schilings.neiko.authorization.common.constant.TokenAttributeNameConstants;
import com.schilings.neiko.authorization.common.constant.UserInfoFiledNameConstants;
import com.schilings.neiko.authorization.common.userdetails.ClientPrincipal;
import com.schilings.neiko.authorization.common.userdetails.User;
import com.schilings.neiko.security.oauth2.core.ScopeNames;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class SecurityUtils {

	private static final String AUTHORIZATION_SERVER_CONFIGURATION_ADAPTER = "com.schilings.neiko.security.oauth2.authorization.server.autoconfigure.AuthorizationServerConfigurationAdapter";

	private static final String RESOUCE_SERVER_CONFIGURATION_ADAPTER = "com.schilings.neiko.security.oauth2.resource.server.autoconfigure.ResourceServerConfigurationAdapter";

	private static final boolean authorizationServerConfigurationAdapterPresent;

	private static final boolean resourceServerConfigurationAdapterPresent;

	private static final boolean sharedStored;

	static {
		authorizationServerConfigurationAdapterPresent = ClassUtils
				.isPresent(AUTHORIZATION_SERVER_CONFIGURATION_ADAPTER, SecurityUtils.class.getClassLoader());
		resourceServerConfigurationAdapterPresent = ClassUtils.isPresent(RESOUCE_SERVER_CONFIGURATION_ADAPTER,
				SecurityUtils.class.getClassLoader());

		sharedStored = authorizationServerConfigurationAdapterPresent && resourceServerConfigurationAdapterPresent;
	}

	/**
	 * 获取Authentication
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取用户详情
	 */
	public User getUser() {
		Authentication authentication = getAuthentication();
		return getUser(authentication);
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
		// BearerTokenAuthentication.getPrincipal()
		// 是OAuth2IntrospectionAuthenticatedPrincipal

		// UsernamePasswordAuthenticationToken or OAuth2LoginAuthenticationToken
		if (authentication.getPrincipal() instanceof User) {
			return (User) authentication.getPrincipal();
		}
		User authenticatedUser = null;
		// 可能有采用共享式
		if (sharedStored) {
			if (authentication instanceof AbstractOAuth2TokenAuthenticationToken<?> authenticationToken) {
				authenticatedUser = getUserSharedStored(authenticationToken);
			}
		}
		// 为null说明不是共享式,解析Token获取User
		if (resourceServerConfigurationAdapterPresent && authenticatedUser == null) {
			if (authentication instanceof AbstractOAuth2TokenAuthenticationToken<?> authenticationToken) {
				authenticatedUser = getFromAuthenticationToken(authenticationToken);
			}
		}
		return authenticatedUser;
	}

	private User getFromAuthenticationToken(AbstractOAuth2TokenAuthenticationToken<?> authentication) {
		// BearerTokenAuthentication or JwtAuthenticationToken
		Map<String, Object> attributes = authentication.getTokenAttributes();
		Map<String, Object> userClaim = (Map<String, Object>) attributes.get(ScopeNames.USER_INFO_CLAIM);
		User.UserBuilder userBuilder = User.builder();
		if (!CollectionUtils.isEmpty(userClaim)) {
			userBuilder = builderFromUserClaim(userClaim);
			return userBuilder.authorities(authentication.getAuthorities()).build();
		}
		return userBuilder.username(authentication.getName()).authorities(authentication.getAuthorities())
				.attributes(authentication.getTokenAttributes()).build();
	}

	private User.UserBuilder builderFromUserClaim(Map<String, Object> userClaim) {
		Map<String, Object> info = (Map<String, Object>) userClaim.get(TokenAttributeNameConstants.INFO);
		Map<String, Object> attributes = (Map<String, Object>) userClaim.get(TokenAttributeNameConstants.ATTRIBUTES);
		User.UserBuilder builder = User.builder();
		if (!CollectionUtils.isEmpty(info)) {
			builder.userId((Long) info.get(UserInfoFiledNameConstants.USER_ID))
					.username((String) info.get(UserInfoFiledNameConstants.USERNAME))
					.avatar((String) info.get(UserInfoFiledNameConstants.AVATAR))
					.nickname((String) info.get(UserInfoFiledNameConstants.NICKNAME))
					.organizationId((Long) info.get(UserInfoFiledNameConstants.ORGANIZATION_ID))
					.type((Integer) info.get(UserInfoFiledNameConstants.TYPE));
		}
		if (!CollectionUtils.isEmpty(attributes)) {
			builder.attributes(attributes);
		}
		return builder;

	}

	private User getUserSharedStored(AbstractOAuth2TokenAuthenticationToken<?> authenticationToken) {
		Object principal = authenticationToken.getTokenAttributes().get(Principal.class.getName());
		if (principal instanceof Authentication authentication) {
			if (authentication.getPrincipal() instanceof User user)
				return user;

			if (authentication.getPrincipal() instanceof UserDetails user)
				return userDetailsToUser(user);

			if (authentication.getPrincipal() instanceof OAuth2User user) {
				return oauth2UserToUser(user);
			}
		}
		return null;
	}

	private User userDetailsToUser(UserDetails userDetails) {
		return User.builder().username(userDetails.getUsername()).password(userDetails.getPassword())
				.authorities(userDetails.getAuthorities()).status(userDetails.isEnabled() ? 1 : null)
				// 传递原本的UserDetails
				.attributes(Collections.singletonMap(UserDetails.class.getName(), userDetails)).build();
	}

	private User oauth2UserToUser(OAuth2User oAuth2User) {
		HashMap<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
		attributes.put(OAuth2User.class.getName(), oAuth2User);
		return User.builder().username(oAuth2User.getName()).authorities(oAuth2User.getAuthorities())
				.attributes(attributes).status(1).build();
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
