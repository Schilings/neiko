package com.schilings.neiko.security.oauth2.resource.server.config;


import com.schilings.neiko.security.oauth2.resource.server.properties.ResourceServerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 因为在OAuth2ResourceServerAutoConfiguration之前加载，可以激活spring resource server的配置
 */
public class JwtDecoderCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConditionMessage.Builder message = ConditionMessage.forCondition("JwtDecoder Condition");
        ConfigurableEnvironment environment = (ConfigurableEnvironment) context.getEnvironment();
        String jwkSetUri = environment.getProperty("neiko.security.oauth2.resourceserver.jwt.jwk-set-uri");
        String issuerUri = environment.getProperty("neiko.security.oauth2.resourceserver.jwt.issuer-uri");
        String publicKeyLocation = environment.getProperty("neiko.security.oauth2.resourceserver.jwt.public-key-location");
        
        
        HashMap<String, Object> properties = new HashMap<>();
        boolean match = false;
        if (StringUtils.hasText(jwkSetUri)) {
            properties.put("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", jwkSetUri);
            match = true;
        }
        if (StringUtils.hasText(issuerUri)) {
            properties.put("spring.security.oauth2.resourceserver.jwt.issuer-uri", issuerUri);
            match = true;
        }
        if (StringUtils.hasText(publicKeyLocation)) {
            properties.put("spring.security.oauth2.resourceserver.jwt.public-key-location", publicKeyLocation);
            match = true;
        }
        
        
        if (match) {
            copyList("neiko.security.oauth2.resourceserver.jwt.jws-algorithms", 
                    "spring.security.oauth2.resourceserver.jwt.jws-algorithms", environment, properties);
            copyList("neiko.security.oauth2.resourceserver.jwt.audiences", 
                    "spring.security.oauth2.resourceserver.jwt.audiences", environment, properties);
            environment.getPropertySources().addFirst(new MapPropertySource(JwtDecoderCondition.class.getSimpleName(), properties));
            return ConditionOutcome.match(message.found("Found Jwt Config Info").items(properties));
        }
        return ConditionOutcome.noMatch(message.because("Not found Jwt Config Info"));
    }

    private void copyList(String sourceName,String targetName,ConfigurableEnvironment environment, Map<String,Object> properties) {
        String sourceNameFormat = sourceName + "[%d]";
        String targetNameFormat = targetName + "[%d]";
        int i = 0;
        for (; ; ) {
            String value = environment.getProperty(String.format(sourceNameFormat, i));
            if (StringUtils.hasText(value)) {
                properties.put(String.format(targetNameFormat, i), value);
                i++;
            } else {
                break;
            } 
        }
    }
}
