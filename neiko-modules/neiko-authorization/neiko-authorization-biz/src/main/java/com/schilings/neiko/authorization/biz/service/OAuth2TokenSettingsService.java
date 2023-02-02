package com.schilings.neiko.authorization.biz.service;

import com.schilings.neiko.authorization.model.dto.OAuth2TokenSettingsDTO;
import com.schilings.neiko.authorization.model.entity.OAuth2TokenSettings;
import com.schilings.neiko.authorization.model.vo.OAuth2TokenSettingsVO;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;


public interface OAuth2TokenSettingsService extends ExtendService<OAuth2TokenSettings> {

	OAuth2TokenSettingsVO getByClientId(String clientId);

	boolean saveOrUpdateTokenSettings(OAuth2TokenSettingsDTO dto);

}
