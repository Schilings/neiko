package com.schilings.neiko.security.oauth2.client;

import com.schilings.neiko.security.oauth2.client.condition.ClientsConfigureCondition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.ClientsConfiguredCondition;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Copy From Spring Security Oauth2 Client {@link Configuration @Configuration} used to
 * map {@link OAuth2ClientProperties} to client registrations.
 *
 * @author Madhura Bhave
 */

@Configuration(proxyBeanMethods = false)
@AutoConfiguration(before = OAuth2ClientAutoConfiguration.class)
public class OAuth2ClientRegistrationRepositoryConfiguration {

	
	@Bean
	@Conditional(ClientsConfiguredCondition.class)
	@ConditionalOnMissingBean(ClientRegistrationRepository.class)
	public InMemoryClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties properties) {
		List<ClientRegistration> registrations = new ArrayList<>(
				OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(properties).values());
		return new InMemoryClientRegistrationRepository(registrations);
	}

	/**
	 * 不同的是ClientsConfigureCondition允许没有配置ClientRegistration也注入ClientRegistrationRepository
	 * 防止OAuth2ClientProperties不注入导致ClientRegistrationRepository也不注入,
	 * 因为InMemoryClientRegistrationRepository不允许ClientRegistration为空
	 * @return
	 */
	@Bean
	@Conditional(ClientsConfigureCondition.class)
	@ConditionalOnMissingBean(ClientRegistrationRepository.class)
	public ClientRegistrationRepository emptyClientRegistrationRepository() {
		return registrationId -> null;
	}
	
}
