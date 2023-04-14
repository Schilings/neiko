package com.schilings.neiko.authorization.biz.tokencustomizer;

import com.schilings.neiko.authorization.common.constant.TokenAttributeNameConstants;
import com.schilings.neiko.authorization.common.constant.UserAttributeNameConstants;
import com.schilings.neiko.authorization.common.constant.UserInfoFiledNameConstants;
import com.schilings.neiko.authorization.common.userdetails.User;
import com.schilings.neiko.security.oauth2.authorization.server.tokencustomizer.OAuth2TokenClaimsContextConsumer;
import com.schilings.neiko.security.oauth2.core.ScopeNames;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserClaimOAuth2TokenClaimsContextConsumer implements OAuth2TokenClaimsContextConsumer {

	@Override
	public void accept(OAuth2TokenClaimsContext context) {
		Set<String> authorizedScopes = context.getAuthorizedScopes();

		// 处理ACCESS_TOKEN
		if (OAuth2TokenType.ACCESS_TOKEN.getValue().equalsIgnoreCase(context.getTokenType().getValue())) {
			// 携带scope = user_info_claim，
			if (!authorizedScopes.contains(ScopeNames.USER_INFO_CLAIM)) {
				return;
			}
			if (context.getAuthorizationGrantType().equals(AuthorizationGrantType.CLIENT_CREDENTIALS)) {
				return;
			}
			Map<String, Object> userClaim = extractUser(context.getPrincipal());
			context.getClaims().claims(existingClaims -> existingClaims.put(ScopeNames.USER_INFO_CLAIM, userClaim));

		}
	}

	private Map<String, Object> extractUser(Authentication authentication) {
		// Authentication可能是UsernamePasswordAuthenticationToken or
		// OAuth2LoginAuthenticationToken
		if (authentication.getPrincipal() instanceof User user) {
			// info
			Map<String, Object> info = Map.of(
					UserInfoFiledNameConstants.USER_ID, user.getUserId(),
					UserInfoFiledNameConstants.TYPE, user.getType(),
					UserInfoFiledNameConstants.ORGANIZATION_ID, user.getOrganizationId(),
					UserInfoFiledNameConstants.USERNAME, user.getUsername(),
					UserInfoFiledNameConstants.NICKNAME, user.getNickname()
			);
			// attributes
			Map<String, Object> attributes = new HashMap<>();
			user.getAttributes().computeIfPresent(UserAttributeNameConstants.ROLE_CODES, (k, v) -> {
				attributes.put(k, v);
				return v;
			});

			// 放入user_info_claim
			// 放入user_info_claim
			return Map.of(TokenAttributeNameConstants.INFO, info, TokenAttributeNameConstants.ATTRIBUTES, attributes);
		}
		return Collections.emptyMap();
	}

}
