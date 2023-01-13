package com.schilings.neiko.security.oauth2.client;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

public enum CommonOAuth2Provider {

	WECHAT_WEB_MP {
		@Override
		public ClientRegistration.Builder getBuilder(String registrationId) {
			ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.NONE,
					DEFAULT_REDIRECT_URL);
			builder.scope("snsapi_userinfo");
			builder.authorizationUri("https://open.weixin.qq.com/connect/oauth2/authorize");
			builder.tokenUri("https://api.weixin.qq.com/sns/oauth2/access_token");
			builder.userInfoUri("https://api.weixin.qq.com/sns/userinfo");
			builder.clientName("Wechat Web MP");
			return builder;
		}
	},

	WECHAT_WEB_QR {
		@Override
		public ClientRegistration.Builder getBuilder(String registrationId) {
			ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.NONE,
					DEFAULT_REDIRECT_URL);
			builder.scope("snsapi_login");
			builder.authorizationUri("https://open.weixin.qq.com/connect/qrconnect");
			builder.tokenUri("https://api.weixin.qq.com/sns/oauth2/access_token");
			builder.userInfoUri("https://api.weixin.qq.com/sns/userinfo");
			builder.clientName("Wechat Web QR");
			return builder;
		}
	},

	WORK_WECHAT_WEB_QR {
		@Override
		public ClientRegistration.Builder getBuilder(String registrationId) {
			ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.NONE,
					DEFAULT_REDIRECT_URL);
			builder.authorizationUri("https://open.work.weixin.qq.com/wwopen/sso/qrConnect");
			builder.tokenUri("https://qyapi.weixin.qq.com/cgi-bin/gettoken");
			builder.userInfoUri("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo");
			builder.clientName("Wechat Web QR");
			return builder;
		}
	},

	GOOGLE {

		@Override
		public ClientRegistration.Builder getBuilder(String registrationId) {
			ClientRegistration.Builder builder = getBuilder(registrationId,
					ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
			builder.scope("openid", "profile", "email");
			builder.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth");
			builder.tokenUri("https://www.googleapis.com/oauth2/v4/token");
			builder.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs");
			builder.issuerUri("https://accounts.google.com");
			builder.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo");
			builder.userNameAttributeName("email");
			builder.clientName("Google");
			return builder;
		}

	},

	GITHUB {

		@Override
		public ClientRegistration.Builder getBuilder(String registrationId) {
			ClientRegistration.Builder builder = getBuilder(registrationId,
					ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
			builder.scope("read:user");
			builder.authorizationUri("https://github.com/login/oauth/authorize");
			builder.tokenUri("https://github.com/login/oauth/access_token");
			builder.userInfoUri("https://api.github.com/user");
			builder.userNameAttributeName("login");
			builder.clientName("GitHub");
			return builder;
		}

	},

	GITEE {
		@Override
		public ClientRegistration.Builder getBuilder(String registrationId) {
			ClientRegistration.Builder builder = getBuilder(registrationId,
					ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
			builder.scope("user_info");
			builder.authorizationUri("https://gitee.com/oauth/authorize");
			builder.tokenUri("https://gitee.com/oauth/token");
			builder.userInfoUri("https://gitee.com/api/v5/user");
			builder.userNameAttributeName("login");
			builder.clientName("Gitee");
			return builder;
		}
	},

	FACEBOOK {

		@Override
		public ClientRegistration.Builder getBuilder(String registrationId) {
			ClientRegistration.Builder builder = getBuilder(registrationId,
					ClientAuthenticationMethod.CLIENT_SECRET_POST, DEFAULT_REDIRECT_URL);
			builder.scope("public_profile", "email");
			builder.authorizationUri("https://www.facebook.com/v2.8/dialog/oauth");
			builder.tokenUri("https://graph.facebook.com/v2.8/oauth/access_token");
			builder.userInfoUri("https://graph.facebook.com/me?fields=id,name,email");
			builder.userNameAttributeName("id");
			builder.clientName("Facebook");
			return builder;
		}

	},

	OKTA {

		@Override
		public ClientRegistration.Builder getBuilder(String registrationId) {
			ClientRegistration.Builder builder = getBuilder(registrationId,
					ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
			builder.scope("openid", "profile", "email");
			builder.userNameAttributeName(IdTokenClaimNames.SUB);
			builder.clientName("Okta");
			return builder;
		}

	};

	private static final String DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/oauth2/code/{registrationId}";

	protected final ClientRegistration.Builder getBuilder(String registrationId, ClientAuthenticationMethod method,
			String redirectUri) {
		ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
		builder.clientAuthenticationMethod(method);
		builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
		builder.redirectUri(redirectUri);
		return builder;
	}

	/**
	 * Create a new
	 * {@link org.springframework.security.oauth2.client.registration.ClientRegistration.Builder
	 * ClientRegistration.Builder} pre-configured with provider defaults.
	 * @param registrationId the registration-id used with the new builder
	 * @return a builder instance
	 */
	public abstract ClientRegistration.Builder getBuilder(String registrationId);

}
