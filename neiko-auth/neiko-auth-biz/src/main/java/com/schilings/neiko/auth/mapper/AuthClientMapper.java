package com.schilings.neiko.auth.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.schilings.neiko.auth.model.entity.AuthClient;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;

public interface AuthClientMapper extends ExtendMapper<AuthClient> {

	/**
	 * 应通过ClientId获取客户端信息
	 * @param clientId ClientId
	 * @return 客户端信息
	 */
	default AuthClient getByClientId(String clientId) {
		return this.selectOne(Wrappers.lambdaQuery(AuthClient.class).eq(AuthClient::getClientId, clientId));
	}

}
