package com.schilings.neiko.samples.admin.federated;


import com.schilings.neiko.security.oauth2.client.federated.identity.OAuth2UserMerger;
import com.schilings.neiko.security.oauth2.client.federated.identity.wechat.WechatOAuth2User;
import com.schilings.neiko.security.oauth2.client.federated.identity.workwechat.WorkWechatOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DefaultOAuth2UserMerger implements OAuth2UserMerger {

    private final UserDetailsService userDetailsService;
    
    
    @Override
    public OAuth2User merge(OAuth2LoginAuthenticationToken authenticationToken) {
        if (authenticationToken.getPrincipal() instanceof DefaultOAuth2User) {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            Map<String, Object> attributes = new HashMap<>();
            String userNameAttributeName = authenticationToken.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authenticationToken.getPrincipal();

            UserDetails userDetails = null;
            try {
                userDetails = userDetailsService.loadUserByUsername(defaultOAuth2User.getName());
            } catch (UsernameNotFoundException e) {
                //TODO 保存
            }
            
            authorities.addAll(defaultOAuth2User.getAuthorities());
            authorities.add(new SimpleGrantedAuthority("SCOPE_" + "message.read"));
            authorities.add(new SimpleGrantedAuthority("SCOPE_" + "message.write"));
            if (userDetails != null) {
                authorities.addAll(userDetails.getAuthorities());
            }
            
            attributes.putAll(defaultOAuth2User.getAttributes());
            
            return new DefaultOAuth2User(authorities, attributes, userNameAttributeName);

        } else if (authenticationToken.getPrincipal() instanceof DefaultOidcUser){
            DefaultOidcUser defaultOAuth2User = (DefaultOidcUser) authenticationToken.getPrincipal();
            return defaultOAuth2User;

        } else if (authenticationToken.getPrincipal() instanceof WechatOAuth2User) {

            
        } else if (authenticationToken.getPrincipal() instanceof WorkWechatOAuth2User) {
            
        }
        return authenticationToken.getPrincipal();
    }
}
