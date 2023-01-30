package com.schilings.neiko.authorization.biz.service;


import com.schilings.neiko.authorization.model.dto.OAuth2ClientSettingsDTO;
import com.schilings.neiko.authorization.model.entity.OAuth2ClientSettings;
import com.schilings.neiko.authorization.model.vo.OAuth2ClientSettingsVO;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;

public interface OAuth2ClientSettingsService extends ExtendService<OAuth2ClientSettings> {

    OAuth2ClientSettingsVO getByClientId(String clientId);

    boolean saveOrUpdateClientSettings(OAuth2ClientSettingsDTO dto);
    
}
