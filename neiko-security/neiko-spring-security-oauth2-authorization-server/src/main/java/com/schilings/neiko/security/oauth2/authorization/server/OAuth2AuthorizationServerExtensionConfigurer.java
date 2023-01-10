package com.schilings.neiko.security.oauth2.authorization.server;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

public abstract class OAuth2AuthorizationServerExtensionConfigurer<C extends OAuth2AuthorizationServerExtensionConfigurer<C, H>, H extends HttpSecurityBuilder<H>>
		extends AbstractHttpConfigurer<C, H> {

}
