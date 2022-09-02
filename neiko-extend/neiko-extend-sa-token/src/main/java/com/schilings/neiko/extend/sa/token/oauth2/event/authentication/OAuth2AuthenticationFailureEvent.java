package com.schilings.neiko.extend.sa.token.oauth2.event.authentication;

import cn.dev33.satoken.exception.SaTokenException;
import com.schilings.neiko.extend.sa.token.holder.RBACAuthorityHolder;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.Authentication;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.UserDetails;

public class OAuth2AuthenticationFailureEvent extends AbstractAuthenticationFailureEvent {

	public OAuth2AuthenticationFailureEvent(SaTokenException exception) {
		super(new FailedOAuthClientAuthentication(), exception);
	}

}

class FailedOAuthClientAuthentication implements Authentication {

	@Override
	public Object getTokenDetails() {
		return null;
	}

	@Override
	public UserDetails getUserDetails() {
		return RBACAuthorityHolder.getUserDetails();
	}

	@Override
	public boolean isAuthenticated() {
		return false;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		return;
	}

}
