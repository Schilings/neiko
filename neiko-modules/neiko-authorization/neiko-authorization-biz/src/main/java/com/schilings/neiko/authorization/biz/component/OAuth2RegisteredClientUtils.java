package com.schilings.neiko.authorization.biz.component;

import com.schilings.neiko.authorization.model.dto.OAuth2ClientSettingsDTO;
import com.schilings.neiko.authorization.model.dto.OAuth2TokenSettingsDTO;
import com.schilings.neiko.authorization.model.vo.OAuth2ClientSettingsVO;
import com.schilings.neiko.authorization.model.vo.OAuth2TokenSettingsVO;
import com.schilings.neiko.common.model.enums.BooleanEnum;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Optional;

public class OAuth2RegisteredClientUtils {

	public static ClientSettings toClientSettings(OAuth2ClientSettingsVO vo) {
		if (vo == null) {
			return ClientSettings.builder().build();
		}
		ClientSettings.Builder builder = ClientSettings.builder()
				.requireProofKey(vo.isRequireProofKey())
				.requireAuthorizationConsent(vo.isRequireAuthorizationConsent());
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.from(vo.getSigningAlgorithm());
		JwsAlgorithm jwsAlgorithm = signatureAlgorithm == null ? MacAlgorithm.from(vo.getSigningAlgorithm())
				: signatureAlgorithm;
		if (jwsAlgorithm != null) {
			builder.tokenEndpointAuthenticationSigningAlgorithm(jwsAlgorithm);
		}
		if (StringUtils.hasText(vo.getJwkSetUrl())) {
			builder.jwkSetUrl(vo.getJwkSetUrl());
		}
		return builder.build();
	}

	public static OAuth2ClientSettingsDTO fromClientSettings(ClientSettings clientSettings) {
		if (clientSettings == null) {
			clientSettings = ClientSettings.builder().build();
		}
		OAuth2ClientSettingsDTO dto = new OAuth2ClientSettingsDTO();
		dto.setRequireProofKey(Optional.of(clientSettings.isRequireProofKey()).filter(bool -> bool)
				.map(bool -> BooleanEnum.TRUE.getValue()).orElse(BooleanEnum.FALSE.getValue()));
		dto.setRequireAuthorizationConsent(Optional.of(clientSettings.isRequireAuthorizationConsent())
				.filter(bool -> bool).map(bool -> BooleanEnum.TRUE.getValue()).orElse(BooleanEnum.FALSE.getValue()));
		dto.setJwkSetUrl(clientSettings.getJwkSetUrl());
		JwsAlgorithm algorithm = clientSettings.getTokenEndpointAuthenticationSigningAlgorithm();
		if (algorithm != null) {
			dto.setSigningAlgorithm(algorithm.getName());
		}
		return dto;
	}

	public static TokenSettings toTokenSettings(OAuth2TokenSettingsVO vo) {
		if (vo == null) {
			return TokenSettings.builder().build();
		}
		return TokenSettings.builder()
				.accessTokenTimeToLive(Optional.ofNullable(vo.getAccessTokenTimeToLive()).orElse(Duration.ofMinutes(5)))
				.authorizationCodeTimeToLive(
						Optional.ofNullable(vo.getAuthorizationCodeTimeToLive()).orElse(Duration.ofMinutes(5)))
				.accessTokenFormat(Optional.ofNullable(vo.getTokenFormat()).map(OAuth2TokenFormat::new)
						.orElse(OAuth2TokenFormat.SELF_CONTAINED))
				.reuseRefreshTokens(vo.isReuseRefreshTokens())
				.refreshTokenTimeToLive(
						Optional.ofNullable(vo.getRefreshTokenTimeToLive()).orElse(Duration.ofMinutes(60)))
				.idTokenSignatureAlgorithm(Optional.ofNullable(vo.getIdTokenSignatureAlgorithm())
						.map(SignatureAlgorithm::from).orElse(SignatureAlgorithm.RS256))
				.build();
	}
	
	public static OAuth2TokenSettingsDTO fromTokenSettings(TokenSettings tokenSettings) {
		if (tokenSettings == null) {
			tokenSettings = TokenSettings.builder().build();
		}
		OAuth2TokenSettingsDTO dto = new OAuth2TokenSettingsDTO();
		dto.setAccessTokenTimeToLive(Optional.ofNullable(tokenSettings.getAccessTokenTimeToLive())
				.orElse(Duration.ofMinutes(5)).toSeconds());
		dto.setAuthorizationCodeTimeToLive(Optional.ofNullable(tokenSettings.getAuthorizationCodeTimeToLive())
				.orElse(Duration.ofMinutes(5)).toSeconds());
		dto.setRefreshTokenTimeToLive(Optional.ofNullable(tokenSettings.getAccessTokenTimeToLive())
				.orElse(Duration.ofMinutes(60)).toSeconds());
		dto.setTokenFormat(tokenSettings.getAccessTokenFormat().getValue());
		dto.setReuseRefreshTokens(
				tokenSettings.isReuseRefreshTokens() ? BooleanEnum.TRUE.getValue() : BooleanEnum.FALSE.getValue());
		dto.setIdTokenSignatureAlgorithm(Optional.ofNullable(tokenSettings.getIdTokenSignatureAlgorithm())
				.map(SignatureAlgorithm::getName).orElse(SignatureAlgorithm.RS256.getName()));
		return dto;
	}

	public static OAuth2TokenSettingsDTO defaultTokenSettings() {
		OAuth2TokenSettingsDTO dto = new OAuth2TokenSettingsDTO();
		dto.setAccessTokenTimeToLive(Duration.ofMinutes(5).toSeconds());
		dto.setAuthorizationCodeTimeToLive(Duration.ofMinutes(5).toSeconds());
		dto.setRefreshTokenTimeToLive(Duration.ofMinutes(60).toSeconds());
		dto.setTokenFormat(OAuth2TokenFormat.SELF_CONTAINED.getValue());
		dto.setReuseRefreshTokens(BooleanEnum.TRUE.getValue());
		dto.setIdTokenSignatureAlgorithm(SignatureAlgorithm.RS256.getName());
		return dto;
	}

	public static OAuth2ClientSettingsDTO defaultClientSettings() {
		OAuth2ClientSettingsDTO dto = new OAuth2ClientSettingsDTO();
		dto.setRequireProofKey(BooleanEnum.FALSE.getValue());
		dto.setRequireAuthorizationConsent(BooleanEnum.FALSE.getValue());
		return dto;
	}

}
