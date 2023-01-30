package com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.client;

import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityConstant;
import com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2EndpointUtils;
import com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2ParameterValidator;
import com.schilings.neiko.security.oauth2.client.resolver.OAuth2AuthorizationRequestCustomizer;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.HashMap;

public class OAuth2FederatedIdentityAuthorizationRequestCustomizer implements OAuth2AuthorizationRequestCustomizer {

	@Override
	public void customize(HttpServletRequest request, OAuth2AuthorizationRequest authorizationRequest) {
		// 如果携带response_type=code
		MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
		String responseType = request.getParameter(OAuth2FederatedIdentityConstant.RESPONSE_TYPE);
		if (StringUtils.hasText(responseType)) {
			// response_type (REQUIRED)
			if (parameters.get(OAuth2FederatedIdentityConstant.RESPONSE_TYPE).size() != 1) {
				throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2FederatedIdentityConstant.RESPONSE_TYPE);
			}
			else if (!responseType.equals(OAuth2FederatedIdentityConstant.CODE)) {
				throwError(OAuth2ErrorCodes.UNSUPPORTED_RESPONSE_TYPE, OAuth2ParameterNames.RESPONSE_TYPE);
			}

			// redirect_uri (REQUIRED)
			String redirectUri = parameters.getFirst(OAuth2FederatedIdentityConstant.REDIRECT_URI);
			//检验redirect_uri规则
			OAuth2ParameterValidator.validateRedirectUri(redirectUri, null);
			if (parameters.get(OAuth2FederatedIdentityConstant.REDIRECT_URI).size() != 1) {
				throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2FederatedIdentityConstant.REDIRECT_URI);
			}

			// client_id (REQUIRED)
			String clientId = parameters.getFirst(OAuth2FederatedIdentityConstant.CLIENT_ID);
			if (!StringUtils.hasText(clientId)
					|| parameters.get(OAuth2FederatedIdentityConstant.CLIENT_ID).size() != 1) {
				throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2FederatedIdentityConstant.CLIENT_ID);
			}
			// attrs.putAll(authorizationRequest.getAdditionalParameters());
			HashMap<String, Object> attrs = new HashMap<>(authorizationRequest.getAttributes());
			// 这个true，作用在OAuth2FederatedIdentityOAuth2AuthorizationRequestRepository
			attrs.put(OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY_REQUEST, "true");
			attrs.put(OAuth2FederatedIdentityConstant.REDIRECT_URI, redirectUri);
			attrs.put(OAuth2FederatedIdentityConstant.CLIENT_ID, clientId);
			Field field = ReflectionUtils.findField(OAuth2AuthorizationRequest.class, "attributes");
			assert field != null;
			ReflectionUtils.makeAccessible(field);
			ReflectionUtils.setField(field, authorizationRequest, attrs);
		}
	}

	private static void throwError(String errorCode, String parameterName) {
		throwError(errorCode, parameterName, "");
	}

	private static void throwError(String errorCode, String parameterName, String errorUri) {
		OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, errorUri);
		throw new OAuth2AuthorizationException(error);
	}

}
