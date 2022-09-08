package com.schilings.neiko.admin.datascope.listener;

import com.schilings.neiko.admin.datascope.component.UserDataScope;
import com.schilings.neiko.admin.datascope.component.UserDataScopeProcessor;
import com.schilings.neiko.common.security.constant.UserAttributeNameConstants;
import com.schilings.neiko.extend.sa.token.core.StpOAuth2UserUtil;
import com.schilings.neiko.extend.sa.token.holder.RBACAuthorityHolder;
import com.schilings.neiko.extend.sa.token.oauth2.event.authentication.AuthenticationSuccessEvent;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.Authentication;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
public class DataScopeAttributeSetter {

	private final UserDataScopeProcessor dataScopeProcessor;

	@Async
	@EventListener(value = AuthenticationSuccessEvent.class)
	public void dataScopeAttributeSetter(AuthenticationSuccessEvent event) {
		Authentication authentication = event.getAuthentication();
		UserDetails userDetails = authentication.getUserDetails();
		UserDataScope userDataScope = dataScopeProcessor.mergeScopeType(Long.valueOf(userDetails.getUserId()),
				StpOAuth2UserUtil.getRoleList(userDetails.getUserId()));
		userDetails.getAttributes().put(UserAttributeNameConstants.USER_DATA_SCOPE, userDataScope);
		RBACAuthorityHolder.setUserDetails(userDetails.getUserId(), userDetails);
	}

}
