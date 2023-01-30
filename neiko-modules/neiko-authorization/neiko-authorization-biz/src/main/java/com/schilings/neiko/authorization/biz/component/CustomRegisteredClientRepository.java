package com.schilings.neiko.authorization.biz.component;

import com.schilings.neiko.authorization.biz.service.OAuth2RegisteredClientService;
import com.schilings.neiko.authorization.model.dto.OAuth2ClientSettingsDTO;
import com.schilings.neiko.authorization.model.dto.OAuth2RegisteredClientDTO;
import com.schilings.neiko.authorization.model.dto.OAuth2TokenSettingsDTO;
import com.schilings.neiko.authorization.model.entity.OAuth2RegisteredClient;
import com.schilings.neiko.authorization.model.vo.OAuth2ClientSettingsVO;
import com.schilings.neiko.authorization.model.vo.OAuth2RegisteredClientInfo;
import com.schilings.neiko.authorization.model.vo.OAuth2TokenSettingsVO;
import com.schilings.neiko.common.model.enums.BooleanEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.schilings.neiko.authorization.biz.component.OAuth2RegisteredClientUtils.*;

@RequiredArgsConstructor
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

    private final OAuth2RegisteredClientService registeredClientService;

    @Override
    public void save(RegisteredClient registeredClient) {
        OAuth2RegisteredClientDTO clientDTO = toDto(registeredClient);
        //Validators.validate(clientDTO);
        OAuth2RegisteredClient client = registeredClientService.getById(Long.valueOf(registeredClient.getId()));
        if (client == null) {
            registeredClientService.saveRegisteredClient(clientDTO);
        } else {
            registeredClientService.updateRegisteredClient(clientDTO);
        } 
        
    }

    @Override
    public RegisteredClient findById(String id) {
        OAuth2RegisteredClientInfo clientInfo = registeredClientService.getClientInfoById(Long.valueOf(id));
        return clientInfo != null ? toObject(clientInfo) : null;
    }
    
    @Override
    public RegisteredClient findByClientId(String clientId) {
        OAuth2RegisteredClientInfo clientInfo = registeredClientService.getClientInfoByClientId(clientId);
        return clientInfo != null ? toObject(clientInfo) : null;
    }

    
    public static OAuth2RegisteredClientDTO toDto(RegisteredClient client) {
        OAuth2RegisteredClientDTO dto = new OAuth2RegisteredClientDTO();
        Instant clientIdIssuedAt = client.getClientIdIssuedAt();
        Instant clientSecretExpiresAt = client.getClientSecretExpiresAt();
        dto.setId(Long.valueOf(client.getId()));
        dto.setClientId(client.getClientId());
        dto.setClientSecret(client.getClientSecret());
        dto.setClientName(client.getClientName());
        if (clientIdIssuedAt != null) {
            dto.setClientIdIssuedAt(clientIdIssuedAt.toString());
        }
        if (clientSecretExpiresAt != null) {
            dto.setClientSecretExpiresAt(clientSecretExpiresAt.toString());
        }
        dto.setClientAuthenticationMethods(client.getClientAuthenticationMethods().stream()
                .map(ClientAuthenticationMethod::getValue).collect(Collectors.toSet()));
        dto.setAuthorizationGrantTypes(client.getAuthorizationGrantTypes().stream()
                .map(AuthorizationGrantType::getValue).collect(Collectors.toSet()));
        dto.setRedirectUris(client.getRedirectUris());
        dto.setScopes(client.getScopes());

        OAuth2ClientSettingsDTO clientSettings = fromClientSettings(null);
        OAuth2TokenSettingsDTO tokenSettings = fromTokenSettings(null);
        clientSettings.setClientId(client.getClientId());
        tokenSettings.setClientId(client.getClientId());
        dto.setClientSettings(clientSettings);
        dto.setTokenSettings(tokenSettings);
        return dto;
    }

    public static RegisteredClient toObject(OAuth2RegisteredClientInfo client) {
        RegisteredClient.Builder builder = RegisteredClient.withId(client.getId().toString())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .clientName(client.getClientName())
                .clientIdIssuedAt(client.getClientIdIssuedAt())
                .clientSecretExpiresAt(client.getClientSecretExpiresAt())
                .clientAuthenticationMethods((authenticationMethods) ->
                        client.getClientAuthenticationMethods().forEach(authenticationMethod ->
                                authenticationMethods.add(resolveClientAuthenticationMethod(authenticationMethod))))
                .authorizationGrantTypes((grantTypes) ->
                        client.getAuthorizationGrantTypes().forEach(grantType ->
                                grantTypes.add(resolveAuthorizationGrantType(grantType))))
                .redirectUris((uris) -> uris.addAll(client.getRedirectUris()))
                .scopes((scopes) -> scopes.addAll(client.getScopes()));


        builder.clientSettings(toClientSettings(client.getClientSettings()));
        builder.tokenSettings(toTokenSettings(client.getTokenSettings()));

        return builder.build();
    }

    public static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        }
        else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        }
        else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        }
        return new AuthorizationGrantType(authorizationGrantType);
    }

    public static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        }
        else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        }
        else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.NONE;
        }
        return new ClientAuthenticationMethod(clientAuthenticationMethod);
    }

}
