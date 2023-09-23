package com.schilings.neiko.security.oauth2.client.condition;

import com.schilings.neiko.security.oauth2.client.CommonOAuth2Provider;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Collections;
import java.util.Map;

public class WechatClientConfiguredCondition extends SpringBootCondition {

	private static final Bindable<Map<String, OAuth2ClientProperties.Registration>> STRING_REGISTRATION_MAP = Bindable
			.mapOf(String.class, OAuth2ClientProperties.Registration.class);

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		ConditionMessage.Builder message = ConditionMessage.forCondition("Wechat OAuth2 Client Configured Condition");
		Map<String, OAuth2ClientProperties.Registration> registrations = getRegistrations(context.getEnvironment());
		if (!registrations.isEmpty()) {
			boolean configured = registrations.containsKey(CommonOAuth2Provider.WECHAT_WEB_MP.name().toLowerCase())
					|| registrations.containsKey(CommonOAuth2Provider.WECHAT_WEB_QR.name().toLowerCase());
			if (configured) {
				return ConditionOutcome.match(message.foundExactly("registered Wechat OAuth2 Client"));
			}
		}
		return ConditionOutcome.noMatch(message.notAvailable("registered clients"));
	}

	private Map<String, OAuth2ClientProperties.Registration> getRegistrations(Environment environment) {
		return Binder.get(environment).bind("spring.security.oauth2.client.registration", STRING_REGISTRATION_MAP)
				.orElse(Collections.emptyMap());
	}

}
