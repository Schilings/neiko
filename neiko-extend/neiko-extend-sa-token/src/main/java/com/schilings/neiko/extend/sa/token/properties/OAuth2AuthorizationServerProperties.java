package com.schilings.neiko.extend.sa.token.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * <p>授权服务器的配置文件</p>
 * 
 * @author Schilings
*/
@Getter
@Setter
@ConfigurationProperties(prefix = OAuth2AuthorizationServerProperties.PREFIX)
public class OAuth2AuthorizationServerProperties {

    public static final String PREFIX = "neiko.sa-token.oauth2.authorization-server";

    /**
     * 登陆验证码开关
     */
    private boolean loginCaptchaEnabled = true;

}
