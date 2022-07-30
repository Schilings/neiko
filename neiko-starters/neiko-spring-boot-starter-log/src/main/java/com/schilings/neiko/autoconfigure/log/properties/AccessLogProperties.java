package com.schilings.neiko.autoconfigure.log.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

@Data
@ConfigurationProperties(prefix = AccessLogProperties.PREFIX)
public class AccessLogProperties {

    public static final String PREFIX = "neiko.log.access";
    
    /**
     * 忽略的Url匹配规则，Ant风格
     */
    private List<String> ignoreUrlPatterns = Arrays.asList("/actuator/**", "/webjars/**", "/favicon.ico",
            "/swagger-ui/**", "/bycdao-ui/**", "/captcha/get");

}
