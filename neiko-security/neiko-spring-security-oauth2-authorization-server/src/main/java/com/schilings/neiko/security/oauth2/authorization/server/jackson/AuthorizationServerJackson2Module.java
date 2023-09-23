package com.schilings.neiko.security.oauth2.authorization.server.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityCode;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

public class AuthorizationServerJackson2Module extends SimpleModule {

	public AuthorizationServerJackson2Module() {
		super(AuthorizationServerJackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
	}

	@Override
	public void setupModule(SetupContext context) {
		context.setMixInAnnotations(Long.class, LongMixin.class);
		context.setMixInAnnotations(OAuth2Authorization.class, OAuth2AuthorizationMixin.class);
		context.setMixInAnnotations(OAuth2FederatedIdentityCode.class, OAuth2FederatedIdentityCodeMixin.class);
	}

}
