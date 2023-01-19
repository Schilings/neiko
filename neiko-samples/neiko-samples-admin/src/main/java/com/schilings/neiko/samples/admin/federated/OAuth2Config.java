package com.schilings.neiko.samples.admin.federated;


import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.schilings.neiko.admin.datascope.component.UserDataScope;
import com.schilings.neiko.authorization.common.jackson2.UserMixin;
import com.schilings.neiko.authorization.common.userdetails.User;
import com.schilings.neiko.samples.admin.jackson.UserDataScopeMixin;
import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerExtensionConfigurer;
import com.schilings.neiko.security.oauth2.authorization.server.configurer.FormLoginRememberMeConfigurer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.client.FederatedIdentityConfigurerConfiguration;
import com.schilings.neiko.security.oauth2.client.federated.identity.FederatedIdentityConfigurer;
import com.schilings.neiko.security.oauth2.client.federated.identity.OAuth2UserMerger;
import com.schilings.neiko.security.oauth2.resource.server.OAuth2ResourceServerConfigurerCustomizer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.*;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;

import java.util.*;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:oauth2-registered-client.properties")
public class OAuth2Config {

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }
    

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper authorizationRowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(registeredClientRepository);
        authorizationRowMapper.setLobHandler(new DefaultLobHandler());
        authorizationRowMapper.setObjectMapper(createObjectMapper());

        JdbcOAuth2AuthorizationService authorizationService = new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
        authorizationService.setAuthorizationRowMapper(authorizationRowMapper);
        return authorizationService;
    }
    
    
    public ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassLoader classLoader = JdbcRegisteredClientRepository.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        objectMapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) {
                context.setMixInAnnotations(User.class, UserMixin.class);
                context.setMixInAnnotations(UserDataScope.class, UserDataScopeMixin.class);
            }
        });
        return objectMapper;
    }
    

    @Bean
    public FormLoginRememberMeConfigurer formLoginRememberMeConfigurer(UserDetailsService userDetailsService) {
        return new FormLoginRememberMeConfigurer(userDetailsService);
    }

    @Bean
    public OAuth2ResourceServerConfigurerCustomizer resourceServerConfigurerCustomizer() {
        return http -> {
            //适配h2 console页面
            http.headers().frameOptions().disable();
        };
    }

    @Bean
    public OAuth2AuthorizationServerConfigurerCustomizer authorizationServerConfigurerCustomizer(OAuth2UserMerger oAuth2UserMerger) {
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
