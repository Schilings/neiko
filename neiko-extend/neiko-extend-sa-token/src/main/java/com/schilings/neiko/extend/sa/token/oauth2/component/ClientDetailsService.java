package com.schilings.neiko.extend.sa.token.oauth2.component;

import com.schilings.neiko.extend.sa.token.oauth2.pojo.ClientDetails;

/**
 *
 * <p>
 * 提供有关 OAuth2 客户端的详细信息的服务。
 * </p>
 *
 * @author Schilings
 */
public interface ClientDetailsService<T extends ClientDetails> {

	/**
	 * 通过客户端 ID 加载客户端。
	 * @param clientId
	 * @return
	 */
	T loadClientByClientId(String clientId);

}
