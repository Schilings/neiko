package com.schilings.neiko.security.oauth2.authorization.server.customizer.oidc.userinfo;

import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;

import java.util.*;

public class DefaultOidcUserInfoMapper implements OidcUserInfoMapper {

	private static final List<String> EMAIL_CLAIMS = Arrays.asList(StandardClaimNames.EMAIL,
			StandardClaimNames.EMAIL_VERIFIED);

	private static final List<String> PHONE_CLAIMS = Arrays.asList(StandardClaimNames.PHONE_NUMBER,
			StandardClaimNames.PHONE_NUMBER_VERIFIED);

	private static final List<String> PROFILE_CLAIMS = Arrays.asList(StandardClaimNames.NAME,
			StandardClaimNames.FAMILY_NAME, StandardClaimNames.GIVEN_NAME, StandardClaimNames.MIDDLE_NAME,
			StandardClaimNames.NICKNAME, StandardClaimNames.PREFERRED_USERNAME, StandardClaimNames.PROFILE,
			StandardClaimNames.PICTURE, StandardClaimNames.WEBSITE, StandardClaimNames.GENDER,
			StandardClaimNames.BIRTHDATE, StandardClaimNames.ZONEINFO, StandardClaimNames.LOCALE,
			StandardClaimNames.UPDATED_AT);

	@Override
	public OidcUserInfo apply(OidcUserInfoAuthenticationContext authenticationContext) {
		OAuth2Authorization authorization = authenticationContext.getAuthorization();
		OidcIdToken idToken = authorization.getToken(OidcIdToken.class).getToken();
		OAuth2AccessToken accessToken = authenticationContext.getAccessToken();
		Map<String, Object> scopeRequestedClaims = getClaimsRequestedByScope(idToken.getClaims(), accessToken.getScopes());

		return new OidcUserInfo(idToken.getClaims());
	}

	private static Map<String, Object> getClaimsRequestedByScope(Map<String, Object> claims,
			Set<String> requestedScopes) {
		Set<String> scopeRequestedClaimNames = new HashSet<>(32);
		scopeRequestedClaimNames.add(StandardClaimNames.SUB);

		if (requestedScopes.contains(OidcScopes.ADDRESS)) {
			scopeRequestedClaimNames.add(StandardClaimNames.ADDRESS);
		}
		if (requestedScopes.contains(OidcScopes.EMAIL)) {
			scopeRequestedClaimNames.addAll(EMAIL_CLAIMS);
		}
		if (requestedScopes.contains(OidcScopes.PHONE)) {
			scopeRequestedClaimNames.addAll(PHONE_CLAIMS);
		}
		if (requestedScopes.contains(OidcScopes.PROFILE)) {
			scopeRequestedClaimNames.addAll(PROFILE_CLAIMS);
		}

		Map<String, Object> requestedClaims = new HashMap<>(claims);
		// 默认只返回sub + [[ADDRESS],[EMAIL_CLAIMS],[PHONE_CLAIMS],[PROFILE_CLAIMS]]
		// 看上面，不同模式scope对应不同组字段
		requestedClaims.keySet().removeIf(claimName -> !scopeRequestedClaimNames.contains(claimName));

		return requestedClaims;
	}

}
