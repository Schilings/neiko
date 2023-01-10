package com.schilings.neiko.security.oauth2.authorization.server.customizer.oidc;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

@RequiredArgsConstructor
public class OidcOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private static final String AUTHORITY_SCOPE_PREFIX = "SCOPE_";

    private final OAuth2AuthorizationService authorizationService;

    private final RegisteredClientRepository registeredClientRepository;
    

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String accessTokenValue) {
        OAuth2Authorization oAuth2Authorization = authorizationService.findByToken(accessTokenValue,
                OAuth2TokenType.ACCESS_TOKEN);
        if (oAuth2Authorization == null) {
            throw new BadOpaqueTokenException("Invalid access token: " + accessTokenValue);
        }
        
        HashMap<String, Object> claims = new HashMap<>();
        //不同的是获取OidcIdToken
        //客户端凭证方式，拿不到OidcIdToken
        OAuth2Authorization.Token<OidcIdToken> idToken = oAuth2Authorization.getToken(OidcIdToken.class);
        if (idToken == null) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_TOKEN);
        }
        claims.putAll(idToken.getClaims());
        //混合
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.SCOPE, (k, v) -> {
            if (v instanceof String) {
                Collection<String> scopes = Arrays.asList(((String) v).split(" "));
                for (String scope : scopes) {
                    authorities.add(new SimpleGrantedAuthority(AUTHORITY_SCOPE_PREFIX + scope));
                }
                return scopes;
            } else if (v instanceof Collection) {
                for (String scope : (Collection<String>)v) {
                    authorities.add(new SimpleGrantedAuthority(AUTHORITY_SCOPE_PREFIX + scope));
                }
            }
            return v;
        });

        AuthorizationGrantType authorizationGrantType = oAuth2Authorization.getAuthorizationGrantType();
        if (AuthorizationGrantType.PASSWORD.equals(authorizationGrantType)) {
            getUserDetailAuthenticatedPrincipal(oAuth2Authorization,authorities);
        }
        
        //应该不用了，拿不到OidcIdToken
        if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(authorizationGrantType)) {
            getClientPrincipal(oAuth2Authorization,authorities);
        }
        
        return new OAuth2IntrospectionAuthenticatedPrincipal(claims, authorities);
    }

    private void getUserDetailAuthenticatedPrincipal(OAuth2Authorization authorization,
                                                     Collection<GrantedAuthority> authorities) {
        AbstractAuthenticationToken authenticationToken = (AbstractAuthenticationToken) authorization.getAttributes().get(Principal.class.getName());
        Object principal = authenticationToken.getPrincipal();
        //添加用户权限
        if (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authenticationToken.getClass())) {
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                authorities.addAll(userDetails.getAuthorities());
            }
        }
    }

    private void getClientPrincipal(OAuth2Authorization authorization, Collection<GrantedAuthority> authorities) {
        String registeredClientId = authorization.getRegisteredClientId();
        RegisteredClient registeredClient = registeredClientRepository.findByClientId(registeredClientId);
    }


}
