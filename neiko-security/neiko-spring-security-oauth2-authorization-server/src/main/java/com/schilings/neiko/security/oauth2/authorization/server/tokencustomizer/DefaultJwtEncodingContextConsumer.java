package com.schilings.neiko.security.oauth2.authorization.server.tokencustomizer;


import com.schilings.neiko.security.oauth2.core.ScopeNames;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;

import java.util.*;


/**
 * @see JwtGenerator
 */
public class DefaultJwtEncodingContextConsumer implements JwtEncodingContextConsumer {
    
    
    @Override
    public void accept(JwtEncodingContext context) {
        AuthorizationGrantType authorizationGrantType = context.getAuthorizationGrantType();
        Set<String> authorizedScopes = context.getAuthorizedScopes();
        
        //处理ACCESS_TOKEN
        if (OAuth2TokenType.ACCESS_TOKEN.getValue().equalsIgnoreCase(context.getTokenType().getValue())) {
            //============ 看 JwtGenerator 思考添加authorities =======
            //Note： 内容过多，容易造成生成的jwt过长，可能几千字符
            //携带scope = authority_info，
            if (!authorizedScopes.contains(ScopeNames.AUTHORITY_INFO)) {
                return;
            }
            //password模式
            if (!AuthorizationGrantType.PASSWORD.equals(authorizationGrantType)) {
                return;
            }
            Set<String> authorities = extractAuthorities(context.getPrincipal());
            context.getClaims().claims(existingClaims->{
                existingClaims.put(ScopeNames.AUTHORITY_INFO, authorities);
            });   
        }
    }

    private Set<String> extractAuthorities(Authentication principal) {
        return new HashSet<>(AuthorityUtils.authorityListToSet(principal.getAuthorities()));
    }
    
}
