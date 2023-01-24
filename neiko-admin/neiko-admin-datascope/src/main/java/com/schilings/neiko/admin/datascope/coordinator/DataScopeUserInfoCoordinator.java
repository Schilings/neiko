package com.schilings.neiko.admin.datascope.coordinator;

import com.schilings.neiko.admin.datascope.component.UserDataScope;
import com.schilings.neiko.admin.datascope.component.UserDataScopeProcessor;

import com.schilings.neiko.authorization.common.constant.UserAttributeNameConstants;
import com.schilings.neiko.system.authentication.UserInfoCoordinator;
import com.schilings.neiko.system.model.dto.UserInfoDTO;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class DataScopeUserInfoCoordinator implements UserInfoCoordinator {

	private final UserDataScopeProcessor dataScopeProcessor;

	@Override
	public Map<String, Object> coordinateAttribute(UserInfoDTO userInfoDTO, Map<String, Object> attribute) {
		UserDataScope userDataScope = dataScopeProcessor.mergeScopeType(userInfoDTO.getSysUser().getUserId(),
				userInfoDTO.getRoleCodes());
		attribute.put(UserAttributeNameConstants.USER_DATA_SCOPE, userDataScope);
		return attribute;
	}

}
