package com.schilings.neiko.autoconfigure.web.api;


import com.schilings.neiko.autoconfigure.web.api.properties.ApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * <pre>{@code
 *
 * }
 * <p>Api自动配置</p>
 * </pre>
 *
 * @author Schilings
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(ApiProperties.class)
@Import(NeikoWebMvcConfiguration.class)
public class ApiAutoConfiguration {
    
    

}
