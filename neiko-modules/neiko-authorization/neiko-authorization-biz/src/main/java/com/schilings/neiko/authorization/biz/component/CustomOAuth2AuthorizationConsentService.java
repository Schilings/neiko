package com.schilings.neiko.authorization.biz.component;


import com.schilings.neiko.authorization.biz.service.AuthorizationConsentService;
import com.schilings.neiko.authorization.model.dto.AuthorizationConsentDTO;
import com.schilings.neiko.authorization.model.entity.AuthorizationConsent;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class CustomOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private final AuthorizationConsentService authorizationConsentService;
    
    private final RegisteredClientRepository registeredClientRepository;
    
    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        AuthorizationConsentDTO dto = toDto(authorizationConsent);
        //Validators.validate(dto);
        authorizationConsentService.saveOrUpdateAuthorizationConsent(dto);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        authorizationConsentService
                .deleteByRegisteredClientIdAndPrincipalName(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        AuthorizationConsent entity = authorizationConsentService.getByRegisteredClientIdAndPrincipalName(registeredClientId, principalName);
        if (entity != null) {
            RegisteredClient registeredClient = this.registeredClientRepository.findById(registeredClientId);
            if (registeredClient == null) {
                throw new DataRetrievalFailureException(
                        "The RegisteredClient with id '" + registeredClientId + "' was not found in the RegisteredClientRepository.");
            }
            return toObject(entity);
        }
        return null;
    }

    public static  AuthorizationConsentDTO toDto(OAuth2AuthorizationConsent consent) {
        AuthorizationConsentDTO dto = new AuthorizationConsentDTO();
        dto.setRegisteredClientId(consent.getRegisteredClientId());
        dto.setPrincipalName(consent.getPrincipalName());
        Set<String> authorities = new HashSet<>();
        for (GrantedAuthority authority : consent.getAuthorities()) {
            authorities.add(authority.getAuthority());
        }
        dto.setAuthorities(StringUtils.collectionToCommaDelimitedString(authorities));
        return dto;
    }

    public static OAuth2AuthorizationConsent toObject(AuthorizationConsent consent) {
        String registeredClientId = consent.getRegisteredClientId();
        OAuth2AuthorizationConsent.Builder builder = OAuth2AuthorizationConsent.withId(
                registeredClientId, consent.getPrincipalName());
        if (consent.getAuthorities() != null) {
            for (String authority : StringUtils.commaDelimitedListToSet(consent.getAuthorities())) {
                builder.authority(new SimpleGrantedAuthority(authority));
            }
        }
        return builder.build();
    }
}
