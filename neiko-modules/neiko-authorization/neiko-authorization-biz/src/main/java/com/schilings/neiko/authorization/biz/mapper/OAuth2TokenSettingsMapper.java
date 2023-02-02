package com.schilings.neiko.authorization.biz.mapper;

import com.schilings.neiko.authorization.model.entity.OAuth2TokenSettings;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;

public interface OAuth2TokenSettingsMapper extends ExtendMapper<OAuth2TokenSettings> {

	default OAuth2TokenSettings selectByClientId(String clientId) {
		return this.selectById(clientId);
	}

	default Integer updateByClientId(OAuth2TokenSettings entity) {
		return this.updateById(entity);
	}

}
