package com.schilings.neiko.sample.oauth2.config;


import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerExtensionConfigurer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.client.FederatedIdentityConfigurerConfiguration;
import com.schilings.neiko.security.oauth2.client.federated.identity.FederatedIdentityConfigurer;
import com.schilings.neiko.security.oauth2.client.federated.identity.OAuth2UserMerger;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class OAuth2ClientConfig {

    @Bean
    public OAuth2AuthorizationServerConfigurerCustomizer clientCustomizer() {
        return (configurer,http) -> {
            //format:off
            FederatedIdentityConfigurer identityConfigurer = new FederatedIdentityConfigurer(http);

            identityConfigurer.filterProccessUri(FederatedIdentityConfigurer.FILTER_PROCESSES_URI);
            identityConfigurer.authorizationRequestUri(FederatedIdentityConfigurer.AUTHORIZATION_REQUEST_PATTERN);

            identityConfigurer.wechatOAuth2Login();
            identityConfigurer.workWechatOAuth2Login();

            identityConfigurer.oauth2UserMerger(new SimpleOAuth2UserMerger());
            //format:on
            //对授权服务端添加联合登录的支持
            FederatedIdentityConfigurerConfiguration.applyDefaultFederatedIdentity(identityConfigurer, http);
            http.apply(identityConfigurer);
        };
    }

    /**
     * 给自动生成的页面OAuth2登录加上redirect_uri
     * @param clientProperties
     * @return
     */
    @Bean
    public LoginPageExtensionConfigurer extensionConfigurer(OAuth2ClientProperties clientProperties) {
        return new LoginPageExtensionConfigurer(clientProperties);
    }
    
    
    @RequiredArgsConstructor
    static class LoginPageExtensionConfigurer extends OAuth2AuthorizationServerExtensionConfigurer<LoginPageExtensionConfigurer, HttpSecurity>{
        private final OAuth2ClientProperties clientProperties;
        
        @Override
        public void configure(HttpSecurity httpSecurity) throws Exception {
            DefaultLoginPageGeneratingFilter loginPageGeneratingFilter = httpSecurity.getSharedObject(DefaultLoginPageGeneratingFilter.class);
            if (loginPageGeneratingFilter != null) {
                Map<String, String> loginUrlToClientName = new HashMap<>();
                clientProperties.getRegistration().forEach((s, v) -> {
                    String authorizationRequestUri = FederatedIdentityConfigurer.AUTHORIZATION_REQUEST_BASE_URI + "/" + s;
                    authorizationRequestUri += "?response_type=code&client_id=messaging-client1&redirect_uri=http://127.0.0.1:9000/oauth2Login";
                    loginUrlToClientName.put(authorizationRequestUri, v.getClientName());
                });
                loginPageGeneratingFilter.setOauth2AuthenticationUrlToClientName(loginUrlToClientName);
            }
        }
    }

    static class SimpleOAuth2UserMerger implements OAuth2UserMerger {
        @Override
        public OAuth2User merge(OAuth2LoginAuthenticationToken authenticationToken) {
            System.out.println(authenticationToken.getPrincipal());

            if (authenticationToken.getPrincipal() instanceof DefaultOAuth2User) {
                DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authenticationToken.getPrincipal();
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                authorities.addAll(defaultOAuth2User.getAuthorities());
                //手动加两个权限
                authorities.add(new SimpleGrantedAuthority("SCOPE_" + "message.read"));
                authorities.add(new SimpleGrantedAuthority("SCOPE_" + "message.write"));
                String userNameAttributeName = authenticationToken.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
                return new DefaultOAuth2User(authorities, defaultOAuth2User.getAttributes(), userNameAttributeName);

            } else if (authenticationToken.getPrincipal() instanceof DefaultOidcUser){
                DefaultOidcUser defaultOAuth2User = (DefaultOidcUser) authenticationToken.getPrincipal();
                return defaultOAuth2User;

            }
            return authenticationToken.getPrincipal();
        }
    }
    
}
