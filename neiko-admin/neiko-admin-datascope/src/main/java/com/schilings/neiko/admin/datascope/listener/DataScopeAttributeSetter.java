package com.schilings.neiko.admin.datascope.listener;


import com.schilings.neiko.admin.datascope.component.UserDataScopeProcessor;

import com.schilings.neiko.authorization.common.event.OAuth2AccessTokenAuthenticationSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;

@RequiredArgsConstructor
public class DataScopeAttributeSetter {

	private final UserDataScopeProcessor dataScopeProcessor;

	@Async
	@EventListener(value = OAuth2AccessTokenAuthenticationSuccessEvent.class)
	public void dataScopeAttributeSetter(OAuth2AccessTokenAuthenticationSuccessEvent event) {
		OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) event.getAuthentication();
//		User userDetails = SecurityUtils.getUser();
//		UserDataScope userDataScope =
//				dataScopeProcessor.mergeScopeType(Long.valueOf(userDetails.getUserId()),
//						StpOAuth2UserUtil.getRoleList(userDetails.getUserId()));
//		userDetails.getAttributes().put(UserAttributeNameConstants.USER_DATA_SCOPE,
//				userDataScope);
//		 RBACAuthorityHolder.setUserDetails(userDetails.getUserId(), userDetails);
	}

}
