package com.schilings.neiko.authorization.biz.mapper;

import com.schilings.neiko.authorization.model.entity.OAuth2ClientSettings;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;

public interface OAuth2ClientSettingsMapper extends ExtendMapper<OAuth2ClientSettings> {

	default OAuth2ClientSettings selectByClientId(String clientId) {
		return this.selectById(clientId);
	}

	default Integer updateByClientId(OAuth2ClientSettings entity) {
		return this.updateById(entity);
	}

}
