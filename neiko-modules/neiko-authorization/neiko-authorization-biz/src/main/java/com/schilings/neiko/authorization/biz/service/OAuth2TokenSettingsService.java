package com.schilings.neiko.authorization.biz.service;

import com.schilings.neiko.authorization.model.dto.OAuth2TokenSettingsDTO;
import com.schilings.neiko.authorization.model.entity.OAuth2TokenSettings;
import com.schilings.neiko.authorization.model.vo.OAuth2TokenSettingsVO;
import com.schilings.neiko.common.model.enums.BooleanEnum;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;

import java.time.Duration;
import java.util.Optional;

public interface OAuth2TokenSettingsService extends ExtendService<OAuth2TokenSettings> {
    OAuth2TokenSettingsVO getByClientId(String clientId);

    boolean saveOrUpdateTokenSettings(OAuth2TokenSettingsDTO dto);
    
}
