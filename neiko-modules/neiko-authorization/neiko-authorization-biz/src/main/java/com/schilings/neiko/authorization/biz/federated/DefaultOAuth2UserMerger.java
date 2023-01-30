package com.schilings.neiko.authorization.biz.federated;

import com.schilings.neiko.security.oauth2.client.federated.identity.OAuth2UserMerger;
import com.schilings.neiko.security.oauth2.client.federated.identity.wechat.WechatOAuth2User;
import com.schilings.neiko.security.oauth2.client.federated.identity.workwechat.WorkWechatOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
public class DefaultOAuth2UserMerger implements OAuth2UserMerger {
	private final ObjectProvider<WechatUserService> wechatUserServiceProvider;
	private final ObjectProvider<WorkWechatUserService> workWechatUserServiceProvider;
	private final ObjectProvider<OidcUserService> oidcUserServiceProvider;
	private final ObjectProvider<OAuth2UserService> oAuth2UserServiceProvider;
	
	@Override
	public OAuth2User merge(OAuth2LoginAuthenticationToken authenticationToken) {
		String userNameAttributeName = authenticationToken.getClientRegistration().getProviderDetails()
				.getUserInfoEndpoint().getUserNameAttributeName();
		if (authenticationToken.getPrincipal() instanceof WechatOAuth2User) {
			WechatOAuth2User wechatOAuth2User = (WechatOAuth2User) authenticationToken.getPrincipal();
			WechatUserService service = wechatUserServiceProvider.getIfAvailable(() -> (user,attributeName) -> user);
			return service.loadUser(wechatOAuth2User,userNameAttributeName);
		}
		if (authenticationToken.getPrincipal() instanceof WorkWechatOAuth2User) {
			WorkWechatOAuth2User workWechatOAuth2User = (WorkWechatOAuth2User) authenticationToken.getPrincipal();
			WorkWechatUserService service = workWechatUserServiceProvider.getIfAvailable(() ->(user,attributeName) -> user);
			return service.loadUser(workWechatOAuth2User,userNameAttributeName);
		}
		if (authenticationToken.getPrincipal() instanceof OidcUser) {
			OidcUser oidcUser = (OidcUser) authenticationToken.getPrincipal();
			OidcUserService service = oidcUserServiceProvider.getIfAvailable(() -> (user,attributeName) -> user);
			return service.loadUser(oidcUser,userNameAttributeName);
		}
		OAuth2User oAuth2User =  authenticationToken.getPrincipal();
		OAuth2UserService service = oAuth2UserServiceProvider.getIfAvailable(() -> (user,attributeName) -> user);
		return service.loadUser(oAuth2User,userNameAttributeName);
	}




}
