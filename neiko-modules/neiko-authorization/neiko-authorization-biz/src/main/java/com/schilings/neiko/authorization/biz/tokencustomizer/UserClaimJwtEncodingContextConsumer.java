package com.schilings.neiko.authorization.biz.tokencustomizer;

import com.schilings.neiko.authorization.common.constant.TokenAttributeNameConstants;
import com.schilings.neiko.authorization.common.constant.UserAttributeNameConstants;
import com.schilings.neiko.authorization.common.constant.UserInfoFiledNameConstants;
import com.schilings.neiko.authorization.common.userdetails.User;
import com.schilings.neiko.security.oauth2.authorization.server.tokencustomizer.JwtEncodingContextConsumer;
import com.schilings.neiko.security.oauth2.core.ScopeNames;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;

import java.util.*;

public class UserClaimJwtEncodingContextConsumer implements JwtEncodingContextConsumer {

	@Override
	public void accept(JwtEncodingContext context) {
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
			Authentication authorizationGrant = context.getAuthorizationGrant();
			Map<String, Object> userClaim = extractUser(context.getPrincipal());
			context.getClaims().claims(existingClaims -> {
				existingClaims.put(ScopeNames.USER_INFO_CLAIM, userClaim);
			});
		}
	}

	private Map<String, Object> extractUser(Authentication authentication) {
		// Authentication可能是UsernamePasswordAuthenticationToken or
		// OAuth2LoginAuthenticationToken
		if (authentication.getPrincipal() instanceof User user) {
			Map<String, Object> userClaim = new HashMap<>();
			// info
			Map<String, Object> info = new HashMap<>();
			info.put(UserInfoFiledNameConstants.USER_ID, user.getUserId());
			info.put(UserInfoFiledNameConstants.TYPE, user.getType());
			info.put(UserInfoFiledNameConstants.ORGANIZATION_ID, user.getOrganizationId());
			info.put(UserInfoFiledNameConstants.USERNAME, user.getUsername());
			info.put(UserInfoFiledNameConstants.NICKNAME, user.getNickname());
			// attributes
			Map<String, Object> attributes = new HashMap<>();
			if (user.getAttributes() != null) {
				user.getAttributes().computeIfPresent(UserAttributeNameConstants.ROLE_CODES, (k, v) -> {
					attributes.put(k, v);
					return v;
				});
			}

			// 放入user_info_claim
			userClaim.put(TokenAttributeNameConstants.INFO, info);
			userClaim.put(TokenAttributeNameConstants.ATTRIBUTES, attributes);
			return userClaim;
		}
		return Collections.emptyMap();
	}

}