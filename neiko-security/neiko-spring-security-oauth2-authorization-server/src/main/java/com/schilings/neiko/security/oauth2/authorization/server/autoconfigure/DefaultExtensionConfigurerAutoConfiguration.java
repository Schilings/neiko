package com.schilings.neiko.security.oauth2.authorization.server.autoconfigure;


import com.schilings.neiko.security.oauth2.authorization.server.configurer.LastTriggeredAuthenticatedConfigurer;
import com.schilings.neiko.security.oauth2.authorization.server.configurer.OAuth2PasswordDecoderConfigurer;
import com.schilings.neiko.security.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *     注入默认的拓展Configurer
 * </p>
 *
 * @author Schilings
 */
@Configuration(proxyBeanMethods = false)
class DefaultExtensionConfigurerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public LastTriggeredAuthenticatedConfigurer lastTriggeredAuthenticatedConfigurer() {
        return new LastTriggeredAuthenticatedConfigurer();
    }

    /**
     * 登录验证码配置
     * @param captchaValidator 验证码验证器
     * @return FilterRegistrationBean<LoginCaptchaFilter>
     */
//    @Bean
//    @ConditionalOnProperty(prefix = OAuth2AuthorizationServerProperties.PREFIX, name = "login-captcha-enabled",
//            havingValue = "true", matchIfMissing = true)
//    public OAuth2LoginCaptchaConfigurer oAuth2LoginCaptchaConfigurer(CaptchaValidator captchaValidator) {
//        return new OAuth2LoginCaptchaConfigurer(captchaValidator);
//    }


    /**
     * password 模式下，密码入参要求 AES 加密。 在进入令牌端点前，通过过滤器进行解密处理。
     * @param securityProperties 安全配置相关
     * @return FilterRegistrationBean<LoginPasswordDecoderFilter>
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = SecurityProperties.PREFIX,name = "password-secret-key")
    public OAuth2PasswordDecoderConfigurer passwordDecoderConfigurer(SecurityProperties securityProperties) {
        return new OAuth2PasswordDecoderConfigurer(securityProperties.getPasswordSecretKey());
    }
}
