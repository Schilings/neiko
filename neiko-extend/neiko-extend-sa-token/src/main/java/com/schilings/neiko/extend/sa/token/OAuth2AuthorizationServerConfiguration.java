package com.schilings.neiko.extend.sa.token;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
import com.schilings.neiko.extend.sa.token.holder.ExtendComponentHolder;
import com.schilings.neiko.extend.sa.token.oauth2.component.*;
import com.schilings.neiko.extend.sa.token.oauth2.filter.ExtendOAuth2RequestFilter;
import com.schilings.neiko.extend.sa.token.properties.OAuth2AuthorizationServerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 
 * <p></p>
 * 
 * @author Schilings
*/
@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(OAuth2AuthorizationServerProperties.class)
public class OAuth2AuthorizationServerConfiguration {

    private final LoginService loginService;

    private final UserDetailsService<?> userDetailsService;

    private final ClientDetailsService<?> clientDetailsService;

    public final ObjectProvider<List<TokenEnhancer>> tokenEnhancersProvider;

    public final ObjectProvider<List<OAuth2Granter>> oAuth2GrantersProvider;

    private final SaTokenConfig saTokenConfig;

    private final SaOAuth2Config saOAuth2Config;


    /**
     * 对Oauth2请求的过滤器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ExtendOAuth2RequestFilter.class)
    @ConditionalOnProperty(name = "neiko.sa-token.enforce-json-filter", havingValue = "true")
    public FilterRegistrationBean<ExtendOAuth2RequestFilter> extendFilterRegistrationBean() {
        FilterRegistrationBean<ExtendOAuth2RequestFilter> registrationBean = new FilterRegistrationBean<>(
                new ExtendOAuth2RequestFilter());
        registrationBean.addUrlPatterns("/oauth2/*");
        return registrationBean;
    }

    @PostConstruct
    public void initSaTokenConfig() {
        // ========Sa-Token
        // 限制只能从Header上读取token
        saTokenConfig.setIsReadCookie(false);
        saTokenConfig.setIsReadBody(false);
        saTokenConfig.setIsReadHead(true);
        // 限制token生成策略
        saTokenConfig.setTokenStyle("random-64");

        // =======OAuth2
        // 提供的oauth2认证流程，暂只适用password和refresh_token
        saOAuth2Config.setIsCode(false);
        saOAuth2Config.setIsPassword(true);
        saOAuth2Config.setIsNewRefresh(true);
        saOAuth2Config.setIsImplicit(false);
        saOAuth2Config.setIsClient(false);
        // LoginService
        saOAuth2Config.setDoLoginHandle(loginService::passwordLogin);// 登录处理函数
    }

    @PostConstruct
    public void initExtendComponentHolder() {
        ExtendComponentHolder.setTokenEnhancersProvider(tokenEnhancersProvider);
        ExtendComponentHolder.setOAuth2GrantersProvider(oAuth2GrantersProvider);
        ExtendComponentHolder.setLoginService(loginService);
        ExtendComponentHolder.setUserDetailsService(userDetailsService);
        ExtendComponentHolder.setClientDetailsService(clientDetailsService);

    }

}
