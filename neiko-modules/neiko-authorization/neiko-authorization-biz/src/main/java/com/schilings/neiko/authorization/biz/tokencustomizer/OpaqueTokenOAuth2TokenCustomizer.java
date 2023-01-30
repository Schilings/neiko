package com.schilings.neiko.authorization.biz.tokencustomizer;


import com.schilings.neiko.security.oauth2.core.ScopeNames;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @see OAuth2AccessTokenGenerator
 */
public class OpaqueTokenOAuth2TokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {
    @Override
    public void customize(OAuth2TokenClaimsContext context) {
        AuthorizationGrantType authorizationGrantType = context.getAuthorizationGrantType();
        Set<String> authorizedScopes = context.getAuthorizedScopes();
        //============ 看 OAuth2AccessTokenGenerator 思考添加authorities =======
        //携带scope = authority_info，
        if (!authorizedScopes.contains(ScopeNames.AUTHORITY_INFO)) {
            return;
        }
        //password模式
        if (!AuthorizationGrantType.PASSWORD.equals(authorizationGrantType)) {
            return;
        }
        Set<String> scopes = extractAuthorities(context.getPrincipal());
        context.getClaims().claims(existingClaims->{
            existingClaims.computeIfPresent(OAuth2ParameterNames.SCOPE, (k, v) -> {
                scopes.addAll((Collection<? extends String>) v);
                return scopes;
            });
            //可能没有scope,正常要求scope=authority_info，不会有scope没有的情况
            existingClaims.put(OAuth2ParameterNames.SCOPE, scopes);
        });
    }

    private Set<String> extractAuthorities(Authentication principal) {
        return new HashSet<>(AuthorityUtils.authorityListToSet(principal.getAuthorities()));
    }
}
