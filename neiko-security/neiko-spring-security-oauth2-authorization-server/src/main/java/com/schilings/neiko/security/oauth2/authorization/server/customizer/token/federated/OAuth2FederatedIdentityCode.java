package com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated;


import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;

import java.time.Instant;

/**
 * 
 * <p>COPY FROM SAS</p>
 * @see OAuth2AuthorizationCode 
 * @author Schilings
*/
public class OAuth2FederatedIdentityCode extends AbstractOAuth2Token {

    public OAuth2FederatedIdentityCode(String tokenValue, Instant issuedAt, Instant expiresAt) {
        super(tokenValue, issuedAt, expiresAt);
    }
}
