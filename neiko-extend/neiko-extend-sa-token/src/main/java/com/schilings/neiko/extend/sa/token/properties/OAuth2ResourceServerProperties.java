package com.schilings.neiko.extend.sa.token.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <p>资源服务器的配置文件，用于配置 token 鉴定方式。</p>
 * 
 * @author Schilings
*/
@Getter
@Setter
@ConfigurationProperties(prefix = OAuth2ResourceServerProperties.PREFIX)
public class OAuth2ResourceServerProperties {

    public static final String PREFIX = "neiko.sa-token.oauth2.resource-server";

    /**
     * 忽略鉴权的 url 列表
     */
    private List<String> ignoreUrls = new ArrayList<>();

    /**
     * 是否强制关闭注解鉴权
     */
    private boolean enforceCancelAuthenticate = false;

    private boolean checkLogin = true;

    private boolean checkRole = true;

    private boolean checkPermission = true;

    private boolean checkSafe = true;

    private boolean checkBasic = true;

    private boolean checkScope = true;


    /**
     * 是否禁止嵌入iframe
     */
    private boolean iframeDeny = true;

    /**
     * 开启表单登录
     */
    private boolean enableFormLogin = false;

    /**
     * 表单登录地址
     */
    private String formLoginPage = null;

    /**
     * 共享存储的token，这种情况下，利用 tokenStore 可以直接获取 token 信息
     */
    private boolean sharedStoredToken = true;

    /**
     * 当 sharedStoredToken 为 false 时生效。 主要用于配置远程端点
     */
    private final Opaquetoken opaqueToken = new Opaquetoken();

    @Getter
    @Setter
    public static class Opaquetoken {

        /**
         * Client id used to authenticate with the token introspection endpoint.
         */
        private String clientId;

        /**
         * Client secret used to authenticate with the token introspection endpoint.
         */
        private String clientSecret;

        /**
         * OAuth 2.0 endpoint through which token introspection is accomplished.
         */
        private String introspectionUri;

    }


}
