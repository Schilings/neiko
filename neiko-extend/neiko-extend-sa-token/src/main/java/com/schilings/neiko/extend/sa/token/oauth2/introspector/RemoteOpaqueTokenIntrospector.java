package com.schilings.neiko.extend.sa.token.oauth2.introspector;


import cn.hutool.core.collection.CollectionUtil;
import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.TokenIntrospectionResponse;
import com.nimbusds.oauth2.sdk.TokenIntrospectionSuccessResponse;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.Audience;
import com.schilings.neiko.common.security.constant.SecurityConstants;
import com.schilings.neiko.extend.sa.token.oauth2.introspector.OpaqueTokenIntrospector;
import com.schilings.neiko.extend.sa.token.oauth2.introspector.exception.BadOpaqueTokenException;
import com.schilings.neiko.extend.sa.token.oauth2.introspector.exception.OAuth2IntrospectionException;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.Authentication;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.ClientDetails;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.UserDetails;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

public class RemoteOpaqueTokenIntrospector implements OpaqueTokenIntrospector {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Converter<String, RequestEntity<?>> requestEntityConverter;

    private RestOperations restOperations;

    private static final String AUTHORITY_SCOPE_PREFIX = "SCOPE_";

    /**
     * Creates a {@code OpaqueTokenAuthenticationProvider} with the provided parameters
     * @param introspectionUri The introspection endpoint uri
     * @param clientId The client id authorized to introspect
     * @param clientSecret The client's secret
     */
    public RemoteOpaqueTokenIntrospector(String introspectionUri, String clientId, String clientSecret) {
        Assert.notNull(introspectionUri, "introspectionUri cannot be null");
        Assert.notNull(clientId, "clientId cannot be null");
        Assert.notNull(clientSecret, "clientSecret cannot be null");
        this.requestEntityConverter = this.defaultRequestEntityConverter(URI.create(introspectionUri));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(clientId, clientSecret));
        this.restOperations = restTemplate;
    }
    


    private Converter<String, RequestEntity<?>> defaultRequestEntityConverter(URI introspectionUri) {
        return token -> {
            HttpHeaders headers = requestHeaders();
            MultiValueMap<String, String> body = requestBody(token);
            return new RequestEntity<>(body, headers, HttpMethod.POST, introspectionUri);
        };
    }


    private HttpHeaders requestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
        return headers;
    }

    private MultiValueMap<String, String> requestBody(String token) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(SecurityConstants.Param.access_token, token);
        return body;
    }

    @Override
    public Authentication introspect(String accessToken){
        RequestEntity<?> requestEntity = this.requestEntityConverter.convert(accessToken);
        if (requestEntity == null) {
            throw new OAuth2IntrospectionException("requestEntityConverter returned a null entity");
        }
        ResponseEntity<String> responseEntity = makeRequest(requestEntity);
        HTTPResponse httpResponse = adaptToNimbusResponse(responseEntity);
        TokenIntrospectionResponse introspectionResponse = parseNimbusResponse(httpResponse);
        TokenIntrospectionSuccessResponse introspectionSuccessResponse = castToNimbusSuccess(introspectionResponse);
        // relying solely on the authorization server to validate this token (not checking
        // 'exp', for example)
        if (!introspectionSuccessResponse.isActive()) {
            this.logger.trace("Did not validate token since it is inactive");
            throw new BadOpaqueTokenException("Provided token isn't active");
        }
        return (Authentication) convertClaimsSet(introspectionSuccessResponse);
    }

    private ResponseEntity<String> makeRequest(RequestEntity<?> requestEntity) {
        try {
            return this.restOperations.exchange(requestEntity, String.class);
        }
        catch (Exception ex) {
            throw new OAuth2IntrospectionException(ex.getMessage(), ex);
        }
    }

    private HTTPResponse adaptToNimbusResponse(ResponseEntity<String> responseEntity) {
        MediaType contentType = responseEntity.getHeaders().getContentType();

        if (contentType == null) {
            this.logger.trace("Did not receive Content-Type from introspection endpoint in response");

            throw new OAuth2IntrospectionException(
                    "Introspection endpoint response was invalid, as no Content-Type header was provided");
        }

        // Nimbus expects JSON, but does not appear to validate this header first.
        if (!contentType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
            this.logger.trace("Did not receive JSON-compatible Content-Type from introspection endpoint in response");

            throw new OAuth2IntrospectionException("Introspection endpoint response was invalid, as content type '"
                    + contentType + "' is not compatible with JSON");
        }

        HTTPResponse response = new HTTPResponse(responseEntity.getStatusCodeValue());
        response.setHeader(HttpHeaders.CONTENT_TYPE, contentType.toString());
        response.setContent(responseEntity.getBody());

        if (response.getStatusCode() != HTTPResponse.SC_OK) {
            this.logger.trace("Introspection endpoint returned non-OK status code");

            throw new OAuth2IntrospectionException(
                    "Introspection endpoint responded with HTTP status code " + response.getStatusCode());
        }
        return response;
    }

    private TokenIntrospectionResponse parseNimbusResponse(HTTPResponse response) {
        try {
            return TokenIntrospectionResponse.parse(response);
        }
        catch (Exception ex) {
            throw new OAuth2IntrospectionException(ex.getMessage(), ex);
        }
    }

    private TokenIntrospectionSuccessResponse castToNimbusSuccess(TokenIntrospectionResponse introspectionResponse) {
        if (!introspectionResponse.indicatesSuccess()) {
            ErrorObject errorObject = introspectionResponse.toErrorResponse().getErrorObject();
            String message = "Token introspection failed with response " + errorObject.toJSONObject().toJSONString();
            this.logger.trace(message);
            throw new OAuth2IntrospectionException(message);
        }
        return (TokenIntrospectionSuccessResponse) introspectionResponse;
    }

    private Object convertClaimsSet(TokenIntrospectionSuccessResponse response) {
        Map<String, Object> claims = new HashMap<>(16);
        if (response.getAudience() != null) {
            List<String> audiences = new ArrayList<>();
            for (Audience audience : response.getAudience()) {
                audiences.add(audience.getValue());
            }
            claims.put("aud", Collections.unmodifiableList(audiences));
        }
        if (response.getClientID() != null) {
            claims.put("client_id", response.getClientID().getValue());
        }
        if (response.getExpirationTime() != null) {
            Instant exp = response.getExpirationTime().toInstant();
            claims.put("exp", exp);
        }
        if (response.getIssueTime() != null) {
            Instant iat = response.getIssueTime().toInstant();
            claims.put("iat", iat);
        }
        if (response.getIssuer() != null) {
            claims.put("iss", issuer(response.getIssuer().getValue()));
        }
        if (response.getNotBeforeTime() != null) {
            claims.put("nbf", response.getNotBeforeTime().toInstant());
        }

        if (response.getScope() != null) {
            List<String> scopes = Collections.unmodifiableList(response.getScope().toStringList());
            claims.put("scope", scopes);
        }

        boolean isClient;
        try {
            isClient = response.getBooleanParameter("is_client");
        }
        catch (ParseException e) {
            logger.warn("自定端点返回的 is_client 属性解析异常: {}, 请求信息：[{}]", e.getMessage(), response.toJSONObject());
            isClient = false;
        }
        return isClient ? buildClient(claims) : buildUser(response.toJSONObject(), claims);

    }

    @SuppressWarnings("unchecked")
    private ClientDetails buildClient(Map<String, Object> claims) {
        String clientId = (String) claims.get("client_id");

        List<String> scopes = null;
        Object scopeValue = claims.get("scope");
        if (scopeValue instanceof List) {
            scopes = (List<String>) scopeValue;
        }

        Collection<String> authorities = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(scopes)) {
            for (String scope : scopes) {
                authorities.add(AUTHORITY_SCOPE_PREFIX + scope);
            }
        }

        ClientDetails clientDetails = null;
        //clientPrincipal.setS(scopes);
        return clientDetails;
    }

    /**
     * 根据返回值信息，反向构建出 User 对象
     * @param responseBody 响应体信息
     * @param claims attributes
     * @return User
     */
    private UserDetails buildUser(JSONObject responseBody, Map<String, Object> claims) {
        return null;
    }

    private URL issuer(String uri) {
        try {
            return new URL(uri);
        }
        catch (Exception ex) {
            throw new OAuth2IntrospectionException(
                    "Invalid " + "iss" + " value: " + uri);
        }
    }
}
