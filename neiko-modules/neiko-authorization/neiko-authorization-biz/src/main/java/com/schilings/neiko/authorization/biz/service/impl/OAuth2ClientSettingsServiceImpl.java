package com.schilings.neiko.authorization.biz.service.impl;

import com.schilings.neiko.authorization.biz.mapper.OAuth2ClientSettingsMapper;
import com.schilings.neiko.authorization.biz.service.OAuth2ClientSettingsService;
import com.schilings.neiko.authorization.converter.OAuth2ClientSettingsConverter;
import com.schilings.neiko.authorization.model.dto.OAuth2ClientSettingsDTO;
import com.schilings.neiko.authorization.model.entity.OAuth2ClientSettings;
import com.schilings.neiko.authorization.model.vo.OAuth2ClientSettingsVO;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OAuth2ClientSettingsServiceImpl extends ExtendServiceImpl<OAuth2ClientSettingsMapper, OAuth2ClientSettings>
		implements OAuth2ClientSettingsService {

	@Override
	public OAuth2ClientSettingsVO getByClientId(String clientId) {
		return OAuth2ClientSettingsConverter.INSTANCE.poToVo(baseMapper.selectByClientId(clientId));
	}

	@Override
	public boolean saveOrUpdateClientSettings(OAuth2ClientSettingsDTO dto) {
		OAuth2ClientSettings entity = OAuth2ClientSettingsConverter.INSTANCE.dtoToPo(dto);
		return saveOrUpdate(entity);
	}

}
