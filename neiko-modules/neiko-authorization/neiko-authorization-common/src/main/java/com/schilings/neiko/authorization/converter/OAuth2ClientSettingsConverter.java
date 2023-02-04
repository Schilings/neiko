package com.schilings.neiko.authorization.converter;

import com.schilings.neiko.authorization.model.dto.OAuth2ClientSettingsDTO;
import com.schilings.neiko.authorization.model.entity.OAuth2ClientSettings;
import com.schilings.neiko.authorization.model.vo.OAuth2ClientSettingsVO;
import com.schilings.neiko.common.model.enums.BooleanEnum;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OAuth2ClientSettingsConverter {

	OAuth2ClientSettingsConverter INSTANCE = Mappers.getMapper(OAuth2ClientSettingsConverter.class);

	@Mappings(value = { @Mapping(target = "requireProofKey", expression = "java(intToBool(po.getRequireProofKey()))"),
			@Mapping(target = "requireAuthorizationConsent",
					expression = "java(intToBool(po.getRequireAuthorizationConsent()))") })
	OAuth2ClientSettingsVO poToVo(OAuth2ClientSettings po);

	OAuth2ClientSettings dtoToPo(OAuth2ClientSettingsDTO dto);

	default boolean intToBool(Integer value) {
		return value == BooleanEnum.TRUE.getValue();
	}

}
