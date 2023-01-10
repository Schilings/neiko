package com.schilings.neiko.security.oauth2.client.federated.identity;


import com.schilings.neiko.security.oauth2.client.federated.identity.wechat.WechatOAuth2AccessTokenResponseClient;
import com.schilings.neiko.security.oauth2.client.federated.identity.wechat.WechatOAuth2AuthorizationRequestResolver;
import com.schilings.neiko.security.oauth2.client.federated.identity.wechat.WechatOAuth2UserService;
import com.schilings.neiko.security.oauth2.client.federated.identity.workwechat.WorkWechatOAuth2AccessTokenResponseClient;
import com.schilings.neiko.security.oauth2.client.federated.identity.workwechat.WorkWechatOAuth2AuthorizationRequestResolver;
import com.schilings.neiko.security.oauth2.client.federated.identity.workwechat.WorkWechatOAuth2UserService;
import com.schilings.neiko.security.oauth2.client.resolver.DelegatingOAuth2AuthorizationRequestResolver;
import com.schilings.neiko.security.oauth2.client.resolver.OAuth2AuthorizationRequestCustomizer;
import com.schilings.neiko.security.oauth2.client.response.DelegatingOAuth2AccessTokenResponseClient;
import com.schilings.neiko.security.oauth2.client.service.DelegatingOAuth2UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.*;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;


import java.lang.reflect.Field;
import java.util.ArrayList;


import java.util.List;
import java.util.function.Consumer;


public final class FederatedIdentityConfigurer extends AbstractHttpConfigurer<FederatedIdentityConfigurer, HttpSecurity> {


    public static final String REGISTRATION_ID_URI_VARIABLE_NAME = DefaultServerOAuth2AuthorizationRequestResolver.DEFAULT_REGISTRATION_ID_URI_VARIABLE_NAME;
    public static final String AUTHORIZATION_REQUEST_BASE_URI = OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;
    public static final String AUTHORIZATION_REQUEST_PATTERN = DefaultServerOAuth2AuthorizationRequestResolver.DEFAULT_AUTHORIZATION_REQUEST_PATTERN;
    public static final String FILTER_PROCESSES_URI = OAuth2LoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI;
    
    private String filterProccessUri = FILTER_PROCESSES_URI;
    private String authorizationRequestUri = AUTHORIZATION_REQUEST_PATTERN;
    
    private final ClientRegistrationRepository clientRegistrationRepository;
    
    private OAuth2AuthorizationRequestResolver authorizationRequestResolver;
    private OAuth2AccessTokenResponseClient accessTokenResponseClient;
    private OAuth2UserService oAuth2UserService;
    private OAuth2AuthorizationRequestCustomizer authorizationRequestCustomizer = (request,builder) -> {
    };
    
    private AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;
    private AuthenticationSuccessHandler successHandler;
    
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    private FederatedIdentityAuthenticationResultConverter auth2AuthenticationResultConverter;
    private OAuth2UserMerger oauth2UserMerger;
    private OAuth2UserMerger oidcUserMerger;
    //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    private List<OAuth2AccessTokenResponseClient> accessTokenResponseClients = new ArrayList<>();
    private List<OAuth2AuthorizationRequestResolver> authorizationRequestResolvers = new ArrayList<>();
    private List<OAuth2UserService> oAuth2UserServices = new ArrayList<>();

    public FederatedIdentityConfigurer(HttpSecurity httpSecurity) {
        ApplicationContext applicationContext = httpSecurity.getSharedObject(ApplicationContext.class);
        ClientRegistrationRepository clientRegistrationRepository = applicationContext.getBean(ClientRegistrationRepository.class);
        Assert.notNull(clientRegistrationRepository, "clientRegistrationRepository can not be null.");
        httpSecurity.setSharedObject(ClientRegistrationRepository.class, clientRegistrationRepository);
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    public FederatedIdentityConfigurer filterProccessUri(String filterProccessUri) {
        Assert.hasText(filterProccessUri, "filterProccessUri cannot be empty");
        this.filterProccessUri = filterProccessUri;
        return this;
    }

    /**
     * @param authorizationRequestUri The authorization request URI for initiating
     *                                the login flow with an external IDP, defaults to {@code
     *                                "/oauth2/authorization/{registrationId}"}
     * @return This configurer for additional configuration
     */
    public FederatedIdentityConfigurer authorizationRequestUri(String authorizationRequestUri) {
        Assert.hasText(authorizationRequestUri, "authorizationRequestUri cannot be empty");
        this.authorizationRequestUri = authorizationRequestUri;
        return this;
    }

    public FederatedIdentityConfigurer authorizationRequestRepository(AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository) {
        this.authorizationRequestRepository = authorizationRequestRepository;
        return this;
    }
    
    public FederatedIdentityConfigurer oauth2UserMerger(OAuth2UserMerger oauth2UserMerger) {
        Assert.notNull(oauth2UserMerger, "oauth2UserMerger cannot be null");
        this.oauth2UserMerger = oauth2UserMerger;
        return this;
    }

    public FederatedIdentityConfigurer oidcUserMerger(OAuth2UserMerger oidcUserMerger) {
        Assert.notNull(this.oidcUserMerger, "oidcUserMerger cannot be null");
        this.oidcUserMerger = oidcUserMerger;
        return this;
    }

    public FederatedIdentityConfigurer successHandler(AuthenticationSuccessHandler successHandler) {
        Assert.notNull(successHandler, "successHandler cannot be null");
        this.successHandler = successHandler;
        return this;
    }


    public FederatedIdentityConfigurer authorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizer authorizationRequestCustomizer) {
        this.authorizationRequestCustomizer = authorizationRequestCustomizer;
        return this;
    }

    public FederatedIdentityConfigurer authorizationRequestResolver(OAuth2AuthorizationRequestResolver authorizationRequestResolver) {
        this.authorizationRequestResolvers.add(authorizationRequestResolver);
        return this;
    }

    

    public FederatedIdentityConfigurer accessTokenResponseClient(OAuth2AccessTokenResponseClient accessTokenResponseClient) {
        this.accessTokenResponseClients.add(accessTokenResponseClient);
        return this;
    }
    

    public FederatedIdentityConfigurer oAuth2UserService(OAuth2UserService userService) {
        this.oAuth2UserServices.add(userService);
        return this;
    }

    
    public FederatedIdentityConfigurer wechatOAuth2Login() {
        String baseUri = this.authorizationRequestUri.replace("/{registrationId}", "");
        this.authorizationRequestResolver(new WechatOAuth2AuthorizationRequestResolver(this.clientRegistrationRepository, baseUri));
        this.accessTokenResponseClient(new WechatOAuth2AccessTokenResponseClient());
        this.oAuth2UserService(new WechatOAuth2UserService());
        return this;
    }

    public FederatedIdentityConfigurer workWechatOAuth2Login() {
        String baseUri = this.authorizationRequestUri.replace("/{registrationId}", "");
        this.authorizationRequestResolver(new WorkWechatOAuth2AuthorizationRequestResolver(this.clientRegistrationRepository, baseUri));
        this.accessTokenResponseClient(new WorkWechatOAuth2AccessTokenResponseClient());
        this.oAuth2UserService(new WorkWechatOAuth2UserService());
        return this;
    }


    @Override
    public void init(HttpSecurity http) throws Exception {
        String baseUri = this.authorizationRequestUri.replace("/{registrationId}", "");
        
        //requestMatcher
        //http.requestMatchers().antMatchers(this.authorizationRequestUri, this.filterProccessUri);

        if (this.authorizationRequestRepository == null) {
            this.authorizationRequestRepository = new HttpSessionOAuth2AuthorizationRequestRepository();
        }
        
        //resolver
        DefaultOAuth2AuthorizationRequestResolver defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, baseUri);
        http.setSharedObject(DefaultOAuth2AuthorizationRequestResolver.class, defaultResolver);
        DelegatingOAuth2AuthorizationRequestResolver resolver = new DelegatingOAuth2AuthorizationRequestResolver(this.authorizationRequestResolvers, defaultResolver);
        resolver.setAuthorizationRequestCustomizer(this.authorizationRequestCustomizer);
        this.authorizationRequestResolver = resolver;

        //responseClient
        DefaultAuthorizationCodeTokenResponseClient defaultTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
        http.setSharedObject(DefaultAuthorizationCodeTokenResponseClient.class, defaultTokenResponseClient);
        this.accessTokenResponseClient = new DelegatingOAuth2AccessTokenResponseClient(this.accessTokenResponseClients, defaultTokenResponseClient);

        //userService
        DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();
        http.setSharedObject(DefaultOAuth2UserService.class, defaultOAuth2UserService);
        this.oAuth2UserService = new DelegatingOAuth2UserService(this.oAuth2UserServices, defaultOAuth2UserService);
        
        //AuthenticationResultConverter 
        this.auth2AuthenticationResultConverter = new FederatedIdentityAuthenticationResultConverter();
        if (this.oauth2UserMerger != null) {
            this.auth2AuthenticationResultConverter.setOAuth2UserHandler(this.oauth2UserMerger);
        }
        if (this.oidcUserMerger != null) {
            this.auth2AuthenticationResultConverter.setOidcUserHandler(this.oidcUserMerger);
        }

        //successHandler
        if (this.successHandler == null) {
            this.successHandler = new FederatedIdentityAuthenticationSuccessHandler();
        }

        //authenticationEntryPoint配置到exceptionHandling会导致DefaultLoginGeneratorFilter不被注册
        // @formatter:off
        http
                .oauth2Login(oauth2Login -> {
                    //redirectionEndpoint(授权码模式的redirect_uri，转发至处理登录的接口)
                    //OAuth2Login为POST"/login/oauth2/code/*"，FormLogin为POST"/login"
                    oauth2Login.redirectionEndpoint(redirectionEndpoint -> {
                        redirectionEndpoint.baseUri(this.filterProccessUri);
                    });

                    //authorizationEndpoint
                    //发起联合登录的入口 默认为/oauth2/authorization
                    oauth2Login.authorizationEndpoint(authorizationEndpoint -> {
                        authorizationEndpoint.baseUri(baseUri);
                        authorizationEndpoint.authorizationRequestResolver(this.authorizationRequestResolver);
                        authorizationEndpoint.authorizationRequestRepository(this.authorizationRequestRepository);
                    });

                    //tokenEndpoint
                    oauth2Login.tokenEndpoint(tokenEndpoint -> {
                        tokenEndpoint.accessTokenResponseClient(this.accessTokenResponseClient);
                    });

                    //userInfoEndpoint
                    oauth2Login.userInfoEndpoint(userInfoEndpoint -> {
                        userInfoEndpoint.userService(this.oAuth2UserService);
                    });

                    //succesHandler
                    oauth2Login.successHandler(this.successHandler);
                });
        // @formatter:on
    }


    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        DefaultLoginPageGeneratingFilter loginPageGeneratingFilter = httpSecurity
                .getSharedObject(DefaultLoginPageGeneratingFilter.class);

        //注意如果开启了RememberMe，如果是RememberMe模式的登录请求(联合登录一般RememberMe，故rememberMe.alwaysRemember(false);)
        //一旦登录成功将会走AbstractAuthenticationProcessingFilter.successfulAuthentication()
        //默认是TokenBasedRememberMeServices
        //例如OAuth2User登录没有password字段，TokenBasedRememberMeServices就会走UserDetailService拿，查不到就报错
        //而且这一步恰巧在	this.successHandler.onAuthenticationSuccess(request, response, authResult);前
        //所以通过反射侵入OAuth2LoginAuthenticationFilter的attemptAuthentication逻辑
        //在验证最后阶段添加内容
        //发生在AbstractAuthenticationProcessingFilter.successfulAuthentication()前
        
        //不过，更重要的是，能在这一步就能将OAuth2Login得到的OAuth2和本地用户的信息融合，合成Authentication
        //放在configure方法(init之后)进行该操作，确保OAuth2LoginConfigurer在此之前已经存在
        OAuth2LoginConfigurer oAuth2LoginConfigurer = httpSecurity.getConfigurer(OAuth2LoginConfigurer.class);
        Field field = ReflectionUtils.findField(OAuth2LoginConfigurer.class, "authFilter");
        field.setAccessible(true);
        OAuth2LoginAuthenticationFilter authFilter = ((OAuth2LoginAuthenticationFilter) ReflectionUtils.getField(field, oAuth2LoginConfigurer));
        authFilter.setAuthenticationResultConverter(this.auth2AuthenticationResultConverter);

        //authFilter.setContinueChainBeforeSuccessfulAuthentication(true);
        
    }
}
