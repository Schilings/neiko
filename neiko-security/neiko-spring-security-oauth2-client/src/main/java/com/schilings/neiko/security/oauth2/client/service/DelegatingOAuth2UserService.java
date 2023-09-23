package com.schilings.neiko.security.oauth2.client.service;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 *
 * <p>
 * {@link DelegatingOAuth2UserService}
 * </p>
 *
 * @author Schilings
 */
public class DelegatingOAuth2UserService<R extends OAuth2UserRequest, U extends OAuth2User>
		implements OAuth2UserService<R, U> {

	private final OAuth2UserService<R, U> defaultOAuth2UserService;

	private final List<OAuth2UserService<R, U>> userServices;

	private final Map<String, OAuth2UserService<R, U>> userServiceMap;

	/**
	 * Constructs a {@code DelegatingOAuth2UserService} using the provided parameters.
	 * @param userServices a {@code List} of {@link OAuth2UserService}(s)
	 */
	public DelegatingOAuth2UserService(List<OAuth2UserService<R, U>> userServices,
			OAuth2UserService<R, U> defaultUserService) {
		Assert.notNull(defaultUserService, "defaultUserService cannot be empty");
		Assert.notEmpty(userServices, "userServices cannot be empty");
		this.defaultOAuth2UserService = defaultUserService;
		this.userServices = Collections.unmodifiableList(new ArrayList<>(userServices));
		this.userServiceMap = Collections.emptyMap();
	}

	/**
	 * Constructs a {@code DelegatingOAuth2UserService} using the provided parameters.
	 * @param userServiceMap a {@code Map} that k -> registrationId ，v->
	 * {@code  OAuth2UserService}
	 */
	public DelegatingOAuth2UserService(Map<String, OAuth2UserService<R, U>> userServiceMap,
			OAuth2UserService<R, U> defaultUserService) {
		Assert.notNull(defaultUserService, "defaultUserService cannot be empty");
		Assert.notEmpty(userServiceMap, "userServiceMap cannot be empty");
		this.defaultOAuth2UserService = defaultUserService;
		this.userServiceMap = Collections.unmodifiableMap(userServiceMap);
		this.userServices = Collections.emptyList();
	}

	@Override
	public U loadUser(R userRequest) throws OAuth2AuthenticationException {
		Assert.notNull(userRequest, "userRequest cannot be null");
		if (CollectionUtils.isEmpty(userServiceMap)) {
			// @formatter:off
            if (!CollectionUtils.isEmpty(this.userServices)) {
                for (OAuth2UserService<R, U> userService : this.userServices) {
                    U u = userService.loadUser(userRequest);
                    if (Objects.nonNull(u)) {
                        return u;
                    }
                }
            }
            return defaultOAuth2UserService.loadUser(userRequest);
            // @formatter:on
		}
		else {
			String registrationId = userRequest.getClientRegistration().getRegistrationId();
			OAuth2UserService<R, U> oAuth2UserService = userServiceMap.get(registrationId);

			if (oAuth2UserService == null) {
				oAuth2UserService = (OAuth2UserService<R, U>) defaultOAuth2UserService;
			}
			return oAuth2UserService.loadUser(userRequest);
		}
	}

}
