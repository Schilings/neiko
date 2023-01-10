package com.schilings.neiko.security.oauth2.resource.server;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

public abstract class OAuth2ResourceServerExtensionConfigurer<C extends OAuth2ResourceServerExtensionConfigurer<C, H>, H extends HttpSecurityBuilder<H>>
		extends AbstractHttpConfigurer<C, H> {

}
