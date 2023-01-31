package com.schilings.neiko.security.oauth2.authorization.server.tokencustomizer;


import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.util.CollectionUtils;

import java.util.List;

@RequiredArgsConstructor
public class DelegatingOpaqueTokenOAuth2TokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

    private final List<OAuth2TokenClaimsContextConsumer> consumerList;
    @Override
    public void customize(OAuth2TokenClaimsContext context) {
        if (!CollectionUtils.isEmpty(consumerList)) {
            for (OAuth2TokenClaimsContextConsumer consumer : consumerList) {
                consumer.accept(context);
            }
        }
    }
}
