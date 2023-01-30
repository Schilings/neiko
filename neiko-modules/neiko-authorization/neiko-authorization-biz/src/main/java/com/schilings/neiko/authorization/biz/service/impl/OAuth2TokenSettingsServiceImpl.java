package com.schilings.neiko.authorization.biz.service.impl;


import com.schilings.neiko.authorization.biz.mapper.OAuth2TokenSettingsMapper;
import com.schilings.neiko.authorization.biz.service.OAuth2TokenSettingsService;
import com.schilings.neiko.authorization.converter.OAuth2TokenSettingsConverter;
import com.schilings.neiko.authorization.model.dto.OAuth2TokenSettingsDTO;
import com.schilings.neiko.authorization.model.entity.OAuth2TokenSettings;
import com.schilings.neiko.authorization.model.vo.OAuth2TokenSettingsVO;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OAuth2TokenSettingsServiceImpl extends ExtendServiceImpl<OAuth2TokenSettingsMapper,OAuth2TokenSettings> 
        implements OAuth2TokenSettingsService {
    
    @Override
    public OAuth2TokenSettingsVO getByClientId(String clientId) {
        OAuth2TokenSettings tokenSettings = baseMapper.selectByClientId(clientId);
        return Objects.nonNull(tokenSettings) ? OAuth2TokenSettingsConverter.INSTANCE.poToVo(tokenSettings) : null;
    }
    

    @Override
    public boolean saveOrUpdateTokenSettings(OAuth2TokenSettingsDTO dto) {
        OAuth2TokenSettings entity = OAuth2TokenSettingsConverter.INSTANCE.dtoToPo(dto);
        return saveOrUpdate(entity);
        
    }
}
