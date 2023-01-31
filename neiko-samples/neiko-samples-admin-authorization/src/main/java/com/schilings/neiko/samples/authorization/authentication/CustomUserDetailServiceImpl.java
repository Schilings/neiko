package com.schilings.neiko.samples.authorization.authentication;


import com.schilings.neiko.common.util.web.WebUtils;
import com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2AuthenticationProviderUtils;
import com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2EndpointUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CustomUserDetailServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        OAuth2ClientAuthenticationToken authenticatedClient = getAuthenticatedClient();
        if (authenticatedClient != null) {
            //如果是OAuth2 密码登录 
            MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(WebUtils.getRequest());
            // scope 
            String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
            Set<String> requestedScopes = null;
            if (StringUtils.hasText(scope)) {
                requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
            }
            
            //可以根据scope返回不同类型的用户
            System.out.println(requestedScopes);
            
        }

        return User.withUsername(username).password("123456")
                .passwordEncoder(passwordEncoder::encode)
                .accountExpired(false).accountLocked(false).disabled(false).credentialsExpired(false)
                .authorities(
                        "ROLE_USER", "ROLE_ADMIN", "ROLE_CUSTOMER", 
                        "system:user:read", "system:user:edit", "system:user:del", "system:user:add",
                        "system:menu:read", "system:menu:edit", "system:menu:del", "system:menu:add",
                        "system:role:read", "system:role:edit", "system:role:del", "system:role:add",
                        "system:organization:read", "system:organization:edit", "system:organization:del", "system:organization:add",
                        "system:config:read", "system:config:edit", "system:config:del", "system:config:add",
                        "system:dict:read", "system:dict:edit", "system:dict:del", "system:dict:add"
                )
                .build();
    }

    private OAuth2ClientAuthenticationToken getAuthenticatedClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            OAuth2ClientAuthenticationToken clientPrincipal = null;
            if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
                clientPrincipal = (OAuth2ClientAuthenticationToken) authentication;
            }
            if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
                clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
            }
            if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
                return clientPrincipal;
            }
        }
        return null;
    }
    
    
    
    
}
