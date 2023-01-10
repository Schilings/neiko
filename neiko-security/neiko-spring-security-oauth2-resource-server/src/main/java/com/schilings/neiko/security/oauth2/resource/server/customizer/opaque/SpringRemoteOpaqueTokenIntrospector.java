package com.schilings.neiko.security.oauth2.resource.server.customizer.opaque;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Instant;
import java.util.*;

/**
 * 
 * <p>Copy From From Spring Security Resource Server For Adaption</p>
 * A Spring implementation of {@link OpaqueTokenIntrospector} that verifies and
 * introspects a token using the configured
 * <a href="https://tools.ietf.org/html/rfc7662" target="_blank">OAuth 2.0 Introspection
 * Endpoint</a>.
 *
 * @author Josh Cummings,Schilings
 * @since 5.6
*/
public class SpringRemoteOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private static final String AUTHORITY_PREFIX = "SCOPE_";

    private static final ParameterizedTypeReference<Map<String, Object>> STRING_OBJECT_MAP = new ParameterizedTypeReference<Map<String, Object>>() {
    };

    private final Log logger = LogFactory.getLog(getClass());

    private final RestOperations restOperations;

    private Converter<String, RequestEntity<?>> requestEntityConverter;


    /**
     * Creates a {@code OpaqueTokenAuthenticationProvider} with the provided parameters
     * @param introspectionUri The introspection endpoint uri
     * @param clientId The client id authorized to introspect
     * @param clientSecret The client's secret
     */
    public SpringRemoteOpaqueTokenIntrospector(String introspectionUri, String clientId, String clientSecret) {
        Assert.notNull(introspectionUri, "introspectionUri cannot be null");
        Assert.notNull(clientId, "clientId cannot be null");
        Assert.notNull(clientSecret, "clientSecret cannot be null");
        this.requestEntityConverter = this.defaultRequestEntityConverter(URI.create(introspectionUri));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(clientId, clientSecret));
        this.restOperations = restTemplate;
    }

    /**
     * Creates a {@code OpaqueTokenAuthenticationProvider} with the provided parameters
     *
     * The given {@link RestOperations} should perform its own client authentication
     * against the introspection endpoint.
     * @param introspectionUri The introspection endpoint uri
     * @param restOperations The client for performing the introspection request
     */
    public SpringRemoteOpaqueTokenIntrospector(String introspectionUri, RestOperations restOperations) {
        Assert.notNull(introspectionUri, "introspectionUri cannot be null");
        Assert.notNull(restOperations, "restOperations cannot be null");
        this.requestEntityConverter = this.defaultRequestEntityConverter(URI.create(introspectionUri));
        this.restOperations = restOperations;
    }

    private Converter<String, RequestEntity<?>> defaultRequestEntityConverter(URI introspectionUri) {
        return (token) -> {
            HttpHeaders headers = requestHeaders();
            MultiValueMap<String, String> body = requestBody(token);
            return new RequestEntity<>(body, headers, HttpMethod.POST, introspectionUri);
        };
    }

    private HttpHeaders requestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private MultiValueMap<String, String> requestBody(String token) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", token);
        return body;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        RequestEntity<?> requestEntity = this.requestEntityConverter.convert(token);
        if (requestEntity == null) {
            throw new OAuth2IntrospectionException("requestEntityConverter returned a null entity");
        }
        ResponseEntity<Map<String, Object>> responseEntity = makeRequest(requestEntity);
        Map<String, Object> claims = adaptToNimbusResponse(responseEntity);
        return convertClaimsSet(claims);
    }

    /**
     * Sets the {@link Converter} used for converting the OAuth 2.0 access token to a
     * {@link RequestEntity} representation of the OAuth 2.0 token introspection request.
     * @param requestEntityConverter the {@link Converter} used for converting to a
     * {@link RequestEntity} representation of the token introspection request
     */
    public void setRequestEntityConverter(Converter<String, RequestEntity<?>> requestEntityConverter) {
        Assert.notNull(requestEntityConverter, "requestEntityConverter cannot be null");
        this.requestEntityConverter = requestEntityConverter;
    }

    private ResponseEntity<Map<String, Object>> makeRequest(RequestEntity<?> requestEntity) {
        try {
            return this.restOperations.exchange(requestEntity, STRING_OBJECT_MAP);
        }
        catch (Exception ex) {
            throw new OAuth2IntrospectionException(ex.getMessage(), ex);
        }
    }

    private Map<String, Object> adaptToNimbusResponse(ResponseEntity<Map<String, Object>> responseEntity) {
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new OAuth2IntrospectionException(
                    "Introspection endpoint responded with " + responseEntity.getStatusCode());
        }
        Map<String, Object> claims = responseEntity.getBody();
        // relying solely on the authorization server to validate this token (not checking
        // 'exp', for example)
        if (claims == null) {
            return Collections.emptyMap();
        }

        boolean active = (boolean) claims.compute(OAuth2TokenIntrospectionClaimNames.ACTIVE, (k, v) -> {
            if (v instanceof String) {
                return Boolean.parseBoolean((String) v);
            }
            if (v instanceof Boolean) {
                return v;
            }
            return false;
        });
        if (!active) {
            this.logger.trace("Did not validate token since it is inactive");
            throw new BadOpaqueTokenException("Provided token isn't active");
        }
        return claims;
    }

    private OAuth2AuthenticatedPrincipal convertClaimsSet(Map<String, Object> claims) {
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.AUD, (k, v) -> {
            if (v instanceof String) {
                return Collections.singletonList(v);
            }
            return v;
        });
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.CLIENT_ID, (k, v) -> v.toString());
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.EXP,
                (k, v) -> Instant.ofEpochSecond(((Number) v).longValue()));
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.IAT,
                (k, v) -> Instant.ofEpochSecond(((Number) v).longValue()));
        // RFC-7662 page 7 directs users to RFC-7519 for defining the values of these
        // issuer fields.
        // https://datatracker.ietf.org/doc/html/rfc7662#page-7
        //
        // RFC-7519 page 9 defines issuer fields as being 'case-sensitive' strings
        // containing
        // a 'StringOrURI', which is defined on page 5 as being any string, but strings
        // containing ':'
        // should be treated as valid URIs.
        // https://datatracker.ietf.org/doc/html/rfc7519#section-2
        //
        // It is not defined however as to whether-or-not normalized URIs should be
        // treated as the same literal
        // value. It only defines validation itself, so to avoid potential ambiguity or
        // unwanted side effects that
        // may be awkward to debug, we do not want to manipulate this value. Previous
        // versions of Spring Security
        // would *only* allow valid URLs, which is not what we wish to achieve here.
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.ISS, (k, v) -> v.toString());
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.NBF,
                (k, v) -> Instant.ofEpochSecond(((Number) v).longValue()));
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.SCOPE, (k, v) -> {
            if (v instanceof String) {
                Collection<String> scopes = Arrays.asList(((String) v).split(" "));
                for (String scope : scopes) {
                    authorities.add(new SimpleGrantedAuthority(AUTHORITY_PREFIX + scope));
                }
                return scopes;
            }
            return v;
        });
        return new OAuth2IntrospectionAuthenticatedPrincipal(claims, authorities);
    }
}
