package com.schilings.neiko.autoconfigure.web.api.configuration;


import com.schilings.neiko.autoconfigure.web.api.properties.ApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
/**
 * <pre>{@code
 *      
 * }
 * <p>Api自动配置</p>
 * </pre>
 * @author Schilings
*/
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(ApiProperties.class)
public class ApiAutoConfiguration {
    
}
