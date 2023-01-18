package com.schilings.neiko.samples.admin.federated;


import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerExtensionConfigurer;
import com.schilings.neiko.security.oauth2.authorization.server.configurer.FormLoginRememberMeConfigurer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.client.FederatedIdentityConfigurerConfiguration;
import com.schilings.neiko.security.oauth2.client.federated.identity.FederatedIdentityConfigurer;
import com.schilings.neiko.security.oauth2.client.federated.identity.OAuth2UserMerger;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OAuth2ClientConfig {

    @Bean
    public FormLoginRememberMeConfigurer formLoginRememberMeConfigurer(UserDetailsService userDetailsService) {
        return new FormLoginRememberMeConfigurer(userDetailsService);
    }

    @Bean
    public OAuth2AuthorizationServerConfigurerCustomizer clientCustomizer(OAuth2UserMerger oAuth2UserMerger) {
        return (configurer,http) -> {
            ObjectPostProcessor postProcessor = http.getSharedObject(ObjectPostProcessor.class);
            FederatedIdentityConfigurer federatedIdentityConfigurer = new FederatedIdentityConfigurer(http);

            //对授权服务端添加联合登录的支持
            FederatedIdentityConfigurerConfiguration.applyDefaultFederatedIdentity(federatedIdentityConfigurer, http);
            //format:off
            http.apply(federatedIdentityConfigurer)
                    .wechatOAuth2Login()
                    .workWechatOAuth2Login()
                    .oauth2UserMerger(oAuth2UserMerger);
            //format:on
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
                    authorizationRequestUri += "?response_type=code&client_id=messaging-client1&redirect_uri=http://localhost:9000/oauth2Login";
                    loginUrlToClientName.put(authorizationRequestUri, v.getClientName());
                });
                loginPageGeneratingFilter.setOauth2AuthenticationUrlToClientName(loginUrlToClientName);
            }
        }
    }
    
    
}
