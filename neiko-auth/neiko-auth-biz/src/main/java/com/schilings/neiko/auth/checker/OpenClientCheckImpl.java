package com.schilings.neiko.auth.checker;

import com.schilings.neiko.auth.properties.AuthProperties;
import com.schilings.neiko.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * <p>
 * 验证是否为开发客户端接口
 * </p>
 *
 * @author Schilings
 */
@Service
@RequiredArgsConstructor
public class OpenClientCheckImpl implements OpenClientChecker {

	private final AuthProperties authProperties;

	@Override
	public boolean isOpenByClientId(String clientId) {
		String id = authProperties.getOpenClient().getClientId();
		return StringUtils.isNotBlank(id) && id.equals(clientId);
	}

	@Override
	public boolean isOpenByAccessToken(String accessToken) {
		String token = authProperties.getOpenClient().getAccessToken();
		return StringUtils.isNotBlank(token) && token.equals(accessToken);
	}

}
