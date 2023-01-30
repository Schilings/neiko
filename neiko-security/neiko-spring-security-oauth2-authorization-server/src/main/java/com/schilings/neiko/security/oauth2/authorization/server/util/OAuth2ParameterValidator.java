package com.schilings.neiko.security.oauth2.authorization.server.util;


import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ERROR_URI;

public final class OAuth2ParameterValidator {

    public static void validateRedirectUri(String requestedRedirectUri,RegisteredClient registeredClient) {
        if (StringUtils.hasText(requestedRedirectUri)) {
            // ***** redirect_uri is available in authorization request
            UriComponents requestedRedirect = null;
            try {
                requestedRedirect = UriComponentsBuilder.fromUriString(requestedRedirectUri).build();
            } catch (Exception ex) { }
            if (requestedRedirect == null || requestedRedirect.getFragment() != null) {
                throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.REDIRECT_URI, registeredClient);
            }

            String requestedRedirectHost = requestedRedirect.getHost();
            if (requestedRedirectHost == null || requestedRedirectHost.equals("localhost")) {
                // As per https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-07#section-9.7.1
                // While redirect URIs using localhost (i.e., "http://localhost:{port}/{path}")
                // function similarly to loopback IP redirects described in Section 10.3.3,
                // the use of "localhost" is NOT RECOMMENDED.
                OAuth2Error error = new OAuth2Error(
                        OAuth2ErrorCodes.INVALID_REQUEST,
                        "localhost is not allowed for the redirect_uri (" + requestedRedirectUri + "). " +
                                "Use the IP literal (127.0.0.1) instead.",
                        "https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-07#section-9.7.1");
                throwError(error, OAuth2ParameterNames.REDIRECT_URI, registeredClient);
            }

            if (registeredClient != null) {
                if (!isLoopbackAddress(requestedRedirectHost)) {
                    // As per https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-07#section-9.7
                    // When comparing client redirect URIs against pre-registered URIs,
                    // authorization servers MUST utilize exact string matching.
                    if (!registeredClient.getRedirectUris().contains(requestedRedirectUri)) {
                        throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.REDIRECT_URI, registeredClient);
                    }
                } else {
                    // As per https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-07#section-10.3.3
                    // The authorization server MUST allow any port to be specified at the
                    // time of the request for loopback IP redirect URIs, to accommodate
                    // clients that obtain an available ephemeral port from the operating
                    // system at the time of the request.
                    boolean validRedirectUri = false;
                    for (String registeredRedirectUri : registeredClient.getRedirectUris()) {
                        UriComponentsBuilder registeredRedirect = UriComponentsBuilder.fromUriString(registeredRedirectUri);
                        registeredRedirect.port(requestedRedirect.getPort());
                        if (registeredRedirect.build().toString().equals(requestedRedirect.toString())) {
                            validRedirectUri = true;
                            break;
                        }
                    }
                    if (!validRedirectUri) {
                        throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.REDIRECT_URI, registeredClient);
                    }
                }
            }

        } else {
            // ***** redirect_uri is NOT available in authorization request
            // redirect_uri is REQUIRED
            throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.REDIRECT_URI, registeredClient);
        }
    }

    public static boolean isLoopbackAddress(String host) {
        // IPv6 loopback address should either be "0:0:0:0:0:0:0:1" or "::1"
        if ("[0:0:0:0:0:0:0:1]".equals(host) || "[::1]".equals(host)) {
            return true;
        }
        // IPv4 loopback address ranges from 127.0.0.1 to 127.255.255.255
        String[] ipv4Octets = host.split("\\.");
        if (ipv4Octets.length != 4) {
            return false;
        }
        try {
            int[] address = new int[ipv4Octets.length];
            for (int i=0; i < ipv4Octets.length; i++) {
                address[i] = Integer.parseInt(ipv4Octets[i]);
            }
            return address[0] == 127 && address[1] >= 0 && address[1] <= 255 && address[2] >= 0 &&
                    address[2] <= 255 && address[3] >= 1 && address[3] <= 255;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private static void throwError(String errorCode, String parameterName,
                                   RegisteredClient registeredClient) {
        OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, ERROR_URI);
        throwError(error, parameterName, registeredClient);
    }

    private static void throwError(OAuth2Error error, String parameterName, RegisteredClient registeredClient) {
        throw new OAuth2AuthenticationException(error);
    }
    
}
