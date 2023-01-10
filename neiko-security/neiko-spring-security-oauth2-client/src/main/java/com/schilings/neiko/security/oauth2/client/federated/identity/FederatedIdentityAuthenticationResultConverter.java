package com.schilings.neiko.security.oauth2.client.federated.identity;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;

public class FederatedIdentityAuthenticationResultConverter implements Converter<OAuth2LoginAuthenticationToken, OAuth2AuthenticationToken> {

    public final Converter<OAuth2LoginAuthenticationToken, OAuth2AuthenticationToken> delegate = this::createAuthenticationResult;

    private OAuth2UserMerger oauth2UserMerger = (authenticationToken) -> null;

    private OAuth2UserMerger oidcUserHandler = (authenticationToken) -> this.oauth2UserMerger.merge(authenticationToken);
    
    @Override
    public OAuth2AuthenticationToken convert(OAuth2LoginAuthenticationToken authentication) {
        String registrationId = authentication.getClientRegistration().getRegistrationId();
        OAuth2User mergedOAuth2User = null;
        if (authentication.getPrincipal() instanceof OidcUser) {
            mergedOAuth2User = this.oidcUserHandler.merge(authentication);
        } else {
            mergedOAuth2User = this.oauth2UserMerger.merge(authentication);
        }
        if (mergedOAuth2User != null) {
            return createAuthenticationResult(mergedOAuth2User, mergedOAuth2User.getAuthorities(), registrationId);
        }
        return delegate.convert(authentication);
    }


    public void setOAuth2UserHandler(OAuth2UserMerger oauth2UserMerger) {
        this.oauth2UserMerger = oauth2UserMerger;
    }

    public void setOidcUserHandler(OAuth2UserMerger oidcUserHandler) {
        this.oidcUserHandler = oidcUserHandler;
    }

    private OAuth2AuthenticationToken createAuthenticationResult(OAuth2User oAuth2User,
                                                                 Collection<? extends GrantedAuthority> authorities,
                                                                 String authorizedClientRegistrationId) {
        return new OAuth2AuthenticationToken(oAuth2User, authorities, authorizedClientRegistrationId);

    }
    
    private OAuth2AuthenticationToken createAuthenticationResult(OAuth2LoginAuthenticationToken authenticationResult) {
        return new OAuth2AuthenticationToken(authenticationResult.getPrincipal(), authenticationResult.getAuthorities(),
                authenticationResult.getClientRegistration().getRegistrationId());

    }
}
