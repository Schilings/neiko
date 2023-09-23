package com.schilings.neiko.security.oauth2.authorization.server.tokencustomizer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.util.CollectionUtils;

import java.util.List;

@RequiredArgsConstructor
public class DelegatingJwtOAuth2TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

	private final List<JwtEncodingContextConsumer> consumerList;

	@Override
	public void customize(JwtEncodingContext context) {
		if (!CollectionUtils.isEmpty(consumerList)) {
			for (JwtEncodingContextConsumer consumer : consumerList) {
				consumer.accept(context);
			}
		}
	}

}
