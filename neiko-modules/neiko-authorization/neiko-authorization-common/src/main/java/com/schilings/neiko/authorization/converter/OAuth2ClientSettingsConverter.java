package com.schilings.neiko.authorization.converter;

import com.schilings.neiko.authorization.model.dto.OAuth2ClientSettingsDTO;
import com.schilings.neiko.authorization.model.entity.OAuth2ClientSettings;
import com.schilings.neiko.authorization.model.vo.OAuth2ClientSettingsVO;
import org.mapstruct.Mapper;

import org.mapstruct.factory.Mappers;

@Mapper
public interface OAuth2ClientSettingsConverter {

    OAuth2ClientSettingsConverter INSTANCE = Mappers.getMapper(OAuth2ClientSettingsConverter.class);

    OAuth2ClientSettingsVO poToVo(OAuth2ClientSettings clientSettings);
    
    OAuth2ClientSettings dtoToPo(OAuth2ClientSettingsDTO dto);
}
