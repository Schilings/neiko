package com.schilings.neiko.security.oauth2.resource.server.config;


import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

public class SharedStoredCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConditionMessage.Builder message = ConditionMessage.forCondition("Spring Authorization Server Shared Stored Condition");
        Environment environment = context.getEnvironment();
        String introspectionUri = environment.getProperty("neiko.security.oauth2.resourceserver.opaquetoken.introspection-uri");
        if (StringUtils.hasText(introspectionUri)) {
            return ConditionOutcome.noMatch(message.found("introspection-uri property").items(introspectionUri));
        }
        return ConditionOutcome.match(message.because("Not found introspection-uri property,choose Shared Stored"));
    }
    
}
