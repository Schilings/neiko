package com.schilings.neiko.autoconfigure.shiro;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ken-Chy129
 * @date 2022/8/3 17:05
 */
@Data
@ConfigurationProperties(prefix = ShiroJWTProperties.PREFIX)
public class ShiroJWTProperties {
    public static final String PREFIX = "neiko.shiro.jwt";
    
    private JWTFilter filter;

    private Redis redis;
    
    @Data
    public static class Redis {
        
        private boolean enabled = true;
        
        // expire time in seconds
        private int expired = 1800;
        
        private String keyPrefix = "neiko:shiro:cache:";
    }
    
    @Data
    public static class JWTFilter {
        
        private String loginUrl = "/login.jsp";

        private String successUrl = "/success.jsp";

        private String unauthorizedUrl = "/unauthorized.jsp";

        private List<String> anonUrlList = Collections.singletonList("/**");

        private List<String> authcUrlList = new ArrayList<>();

        private List<String> jwtUrlList = new ArrayList<>();
    }
    
}
