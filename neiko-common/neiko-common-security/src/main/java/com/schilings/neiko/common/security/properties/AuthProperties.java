package com.schilings.neiko.common.security.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "neiko.auth")
public class AuthProperties {

    /**
     * 前后端交互使用的AES对称加密算法的密钥，必须 16 位字符
     */
    private String passwordSecretKey;

    /**
     * 忽略登录认证的url
     */
    private List<String> ignoreUrls = new ArrayList<>();

    
    private AuthorityCache authorityCache = new AuthorityCache();
    
    
    @Data
    public static class AuthorityCache{

        /**
         * 权限缓存超时时间（单位秒），默认2个小时
         */
        private long timeout = 60 * 60 * 2;
    }
}
