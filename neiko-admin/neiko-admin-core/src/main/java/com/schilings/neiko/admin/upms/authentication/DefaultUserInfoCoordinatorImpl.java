package com.schilings.neiko.admin.upms.authentication;

import com.schilings.neiko.system.model.dto.UserInfoDTO;

import java.util.Map;

/**
 * 默认的用户信息协调者
 */
public class DefaultUserInfoCoordinatorImpl implements UserInfoCoordinator {

	@Override
	public Map<String, Object> coordinateAttribute(UserInfoDTO userInfoDTO, Map<String, Object> attribute) {
		return attribute;
	}

}
