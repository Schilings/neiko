package com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated;


import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.OAuth2ExtensionGrantTypeAuthenticationConverter;
import com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2EndpointUtils;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.web.*;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;


import javax.servlet.http.HttpServletRequest;

import java.util.*;


public class OAuth2FederatedIdentityAuthenticationConverter implements OAuth2ExtensionGrantTypeAuthenticationConverter {
    
    private PortResolver portResolver = new PortResolverImpl();
    private PortMapper portMapper = new PortMapperImpl();
    
    @SneakyThrows
    @Override
    public Authentication convert(HttpServletRequest request) {
        // grant_type (REQUIRED)
        //只处理federated_identity授权方式
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY.getValue().equals(grantType)) {
            return null;
        }

        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

        // code (REQUIRED)
        String code = parameters.getFirst(OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY_AUTHENTICATED_CODE);
        if (StringUtils.hasText(code) && parameters.get(OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY_AUTHENTICATED_CODE).size() != 1) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST,OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY_AUTHENTICATED_CODE,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        
        
        // scope (OPTIONAL)
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.SCOPE,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        Set<String> requestedScopes = null;
        if (StringUtils.hasText(scope)) {
            requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }
        
        //ClientAuthentication //OAuth2ClientAuthenticationToken
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        if (clientPrincipal == null) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ErrorCodes.INVALID_CLIENT,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        
        //在里面放着registrationId,要在AuthenticationProvider校验
        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.SCOPE) &&
                    !key.equals(OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY_AUTHENTICATED_CODE)) {
                additionalParameters.put(key, value.get(0));
            }
        });
        
        return new OAuth2FederatedIdentityAuthenticationToken(code, clientPrincipal, requestedScopes,
                additionalParameters);
        
    }
    
}
