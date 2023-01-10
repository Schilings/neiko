package com.schilings.neiko.security.oauth2.resource.server.customizer;


import com.schilings.neiko.security.oauth2.resource.server.OAuth2ResourceServerConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.resource.server.OAuth2ResourceServerExtensionConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class OAuth2ResourceServerExtensionConfigurerInjectionCustomizer implements OAuth2ResourceServerConfigurerCustomizer {
    
    private final List<OAuth2ResourceServerExtensionConfigurer> extensionConfigurers;

    public OAuth2ResourceServerExtensionConfigurerInjectionCustomizer(
            List<OAuth2ResourceServerExtensionConfigurer> extensionConfigurers) {
        this.extensionConfigurers = extensionConfigurers;
    }

    @Override
    public void customize(HttpSecurity httpSecurity) throws Exception {
        if (!CollectionUtils.isEmpty(this.extensionConfigurers)) {
            AnnotationAwareOrderComparator.sort(this.extensionConfigurers);
            for (OAuth2ResourceServerExtensionConfigurer configurer : this.extensionConfigurers) {
                httpSecurity.apply(configurer);
            }
        }
    }
}
