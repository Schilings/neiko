package com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated;

import org.springframework.lang.Nullable;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.time.Instant;
import java.util.Base64;

/**
 *
 * <p>
 * Copy From SAS
 * </p>
 *
 * @see org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeGenerator
 * @author Schilings
 */
public class OAuth2FederatedIdentityCodeGenerator implements OAuth2TokenGenerator<OAuth2FederatedIdentityCode> {

	private final StringKeyGenerator authorizationCodeGenerator = new Base64StringKeyGenerator(
			Base64.getUrlEncoder().withoutPadding(), 96);

	@Nullable
	@Override
	public OAuth2FederatedIdentityCode generate(OAuth2TokenContext context) {
		if (context.getTokenType() == null
				|| !OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY_CODE.equals(context.getTokenType().getValue())) {
			return null;
		}
		Instant issuedAt = Instant.now();
		// 暂时写死一分钟
		// Instant expiresAt = issuedAt.plus(Duration.of(1, ChronoUnit.MINUTES));
		Instant expiresAt = issuedAt
				.plus(context.getRegisteredClient().getTokenSettings().getAuthorizationCodeTimeToLive());
		return new OAuth2FederatedIdentityCode(this.authorizationCodeGenerator.generateKey(), issuedAt, expiresAt);
	}

}
