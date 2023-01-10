package com.schilings.neiko.security.oauth2.resource.server.customizer.opaque;


import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;


import java.security.Principal;
import java.util.*;

@RequiredArgsConstructor
public class SpringAuthorizationServerSharedStoredOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

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
        claims.putAll(oAuth2Authorization.getToken(OAuth2AccessToken.class).getClaims());
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
        oAuth2Authorization.getAuthorizedScopes().forEach(scope -> {
            authorities.add(new SimpleGrantedAuthority(AUTHORITY_SCOPE_PREFIX + scope));
        });

        AuthorizationGrantType authorizationGrantType = oAuth2Authorization.getAuthorizationGrantType();
        if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(authorizationGrantType)) {
            getClientPrincipal(oAuth2Authorization,authorities);
        }
        if (AuthorizationGrantType.PASSWORD.equals(authorizationGrantType)) {
            getUserDetailAuthenticatedPrincipal(oAuth2Authorization,authorities);
        }
        
        if (OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY_AUTHENTICATED_CODE_TOKEN_TYPE.equals(authorizationGrantType)) {
            getOAuth2UserAuthenticatedPrincipal(oAuth2Authorization,authorities);
        }
        
        return new OAuth2IntrospectionAuthenticatedPrincipal(claims, authorities);
    }

    private void getOAuth2UserAuthenticatedPrincipal(OAuth2Authorization authorization, Collection<GrantedAuthority> authorities) {
        AbstractAuthenticationToken authenticationToken = (AbstractAuthenticationToken) authorization
                .getAttributes().get(Principal.class.getName());
        Object principal = authenticationToken.getPrincipal();
        //添加用户权限
        if (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authenticationToken.getClass())) {
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                authorities.addAll(userDetails.getAuthorities());
            }
        }
    }


    private void getUserDetailAuthenticatedPrincipal(OAuth2Authorization authorization,
                                                     Collection<GrantedAuthority> authorities) {
        AbstractAuthenticationToken authenticationToken = (AbstractAuthenticationToken) authorization
                .getAttributes().get(Principal.class.getName());
        Object principal = authenticationToken.getPrincipal();
        //添加用户权限
        if (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authenticationToken.getClass())) {
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                authorities.addAll(userDetails.getAuthorities());
            }
        }

        
    }

    private void getClientPrincipal(OAuth2Authorization authorization,
                                    Collection<GrantedAuthority> authorities) {
        
        String registeredClientId = authorization.getRegisteredClientId();
        RegisteredClient registeredClient = registeredClientRepository.findByClientId(registeredClientId);
    }


}
