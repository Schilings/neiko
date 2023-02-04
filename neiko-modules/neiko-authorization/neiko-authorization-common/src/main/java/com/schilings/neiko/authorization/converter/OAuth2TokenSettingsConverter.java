package com.schilings.neiko.authorization.converter;

import com.schilings.neiko.authorization.model.dto.OAuth2TokenSettingsDTO;
import com.schilings.neiko.authorization.model.entity.OAuth2TokenSettings;
import com.schilings.neiko.authorization.model.vo.OAuth2TokenSettingsVO;
import com.schilings.neiko.common.model.enums.BooleanEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.Duration;

@Mapper
public interface OAuth2TokenSettingsConverter {

	OAuth2TokenSettingsConverter INSTANCE = Mappers.getMapper(OAuth2TokenSettingsConverter.class);

	@Mappings(value = {
			@Mapping(target = "accessTokenTimeToLive",
					expression = "java(stringSecondToDuration(tokenSettings.getAccessTokenTimeToLive()))"),
			@Mapping(target = "refreshTokenTimeToLive",
					expression = "java(stringSecondToDuration(tokenSettings.getRefreshTokenTimeToLive()))"),
			@Mapping(target = "authorizationCodeTimeToLive",
					expression = "java(stringSecondToDuration(tokenSettings.getAuthorizationCodeTimeToLive()))"),
			@Mapping(target = "reuseRefreshTokens",
					expression = "java(intToBool(tokenSettings.getReuseRefreshTokens()))") })
	OAuth2TokenSettingsVO poToVo(OAuth2TokenSettings tokenSettings);

	OAuth2TokenSettings dtoToPo(OAuth2TokenSettingsDTO dto);

	default Duration stringSecondToDuration(Long seconds) {
		return Duration.ofSeconds(seconds);
	}

	default boolean intToBool(Integer value) {
		return value == BooleanEnum.TRUE.getValue();
	}

}
