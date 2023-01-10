package com.schilings.neiko.security.oauth2.client;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.core.convert.ConversionException;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Copy From Spring Security Oauth2 Client
 * Adapter class to convert {@link OAuth2ClientProperties} to a
 * {@link ClientRegistration}.
 *
 * @author Phillip Webb
 * @author Thiago Hirata
 * @author Madhura Bhave
 * @author MyeongHyeon Lee
 * @since 2.1.0
 */
public final class OAuth2ClientPropertiesRegistrationAdapter {

    private OAuth2ClientPropertiesRegistrationAdapter() {
    }

    public static Map<String, ClientRegistration> getClientRegistrations(OAuth2ClientProperties properties) {
        Map<String, ClientRegistration> clientRegistrations = new HashMap<>();
        properties.getRegistration().forEach((key, value) -> clientRegistrations.put(key,
                getClientRegistration(key, value, properties.getProvider())));
        return clientRegistrations;
    }

    private static ClientRegistration getClientRegistration(String registrationId,
                                                            OAuth2ClientProperties.Registration properties, Map<String, OAuth2ClientProperties.Provider> providers) {
        ClientRegistration.Builder builder = getBuilderFromIssuerIfPossible(registrationId, properties.getProvider(), providers);
        if (builder == null) {
            builder = getBuilder(registrationId, properties.getProvider(), providers);
        }
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        map.from(properties::getClientId).to(builder::clientId);
        map.from(properties::getClientSecret).to(builder::clientSecret);
        map.from(properties::getClientAuthenticationMethod).as(ClientAuthenticationMethod::new)
                .to(builder::clientAuthenticationMethod);
        map.from(properties::getAuthorizationGrantType).as(AuthorizationGrantType::new)
                .to(builder::authorizationGrantType);
        map.from(properties::getRedirectUri).to(builder::redirectUri);
        map.from(properties::getScope).as(StringUtils::toStringArray).to(builder::scope);
        map.from(properties::getClientName).to(builder::clientName);
        return builder.build();
    }

    private static ClientRegistration.Builder getBuilderFromIssuerIfPossible(String registrationId, String configuredProviderId,
                                                                             Map<String, OAuth2ClientProperties.Provider> providers) {
        String providerId = (configuredProviderId != null) ? configuredProviderId : registrationId;
        if (providers.containsKey(providerId)) {
            OAuth2ClientProperties.Provider provider = providers.get(providerId);
            String issuer = provider.getIssuerUri();
            if (issuer != null) {
                ClientRegistration.Builder builder = ClientRegistrations.fromIssuerLocation(issuer).registrationId(registrationId);
                return getBuilder(builder, provider);
            }
        }
        return null;
    }

    private static ClientRegistration.Builder getBuilder(String registrationId, String configuredProviderId,
                                                         Map<String, OAuth2ClientProperties.Provider> providers) {
        //如果providerId为空
        String providerId = (configuredProviderId != null) ? configuredProviderId : registrationId;
        CommonOAuth2Provider provider = getCommonProvider(providerId);
        if (provider == null && !providers.containsKey(providerId)) {
            throw new IllegalStateException(getErrorMessage(configuredProviderId, registrationId));
        }
        ClientRegistration.Builder builder = (provider != null) ? provider.getBuilder(registrationId)
                : ClientRegistration.withRegistrationId(registrationId);
        if (providers.containsKey(providerId)) {
            return getBuilder(builder, providers.get(providerId));
        }
        return builder;
    }

    private static String getErrorMessage(String configuredProviderId, String registrationId) {
        return ((configuredProviderId != null) ? "Unknown provider ID '" + configuredProviderId + "'"
                : "Provider ID must be specified for client registration '" + registrationId + "'");
    }

    private static ClientRegistration.Builder getBuilder(ClientRegistration.Builder builder, OAuth2ClientProperties.Provider provider) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        map.from(provider::getAuthorizationUri).to(builder::authorizationUri);
        map.from(provider::getTokenUri).to(builder::tokenUri);
        map.from(provider::getUserInfoUri).to(builder::userInfoUri);
        map.from(provider::getUserInfoAuthenticationMethod).as(AuthenticationMethod::new)
                .to(builder::userInfoAuthenticationMethod);
        map.from(provider::getJwkSetUri).to(builder::jwkSetUri);
        map.from(provider::getUserNameAttribute).to(builder::userNameAttributeName);
        return builder;
    }

    private static CommonOAuth2Provider getCommonProvider(String providerId) {
        try {
            return ApplicationConversionService.getSharedInstance().convert(providerId, CommonOAuth2Provider.class);
        } catch (ConversionException ex) {
            return null;
        }
    }
}