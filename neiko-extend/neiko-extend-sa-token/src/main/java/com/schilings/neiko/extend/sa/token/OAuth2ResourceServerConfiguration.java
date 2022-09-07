package com.schilings.neiko.extend.sa.token;


import com.schilings.neiko.extend.sa.token.properties.OAuth2ResourceServerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 
 * <p></p>
 * 
 * @author Schilings
*/
@Import(ExtendStrategyConfiguration.class)
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableConfigurationProperties(OAuth2ResourceServerProperties.class)
public class OAuth2ResourceServerConfiguration {


}
