package com.schilings.neiko.samples.starter.datascope.datapermission;

import com.schilings.neiko.common.datascope.annotation.DataPermission;
import com.schilings.neiko.common.datascope.core.DataPermissionRule;
import com.schilings.neiko.common.datascope.core.DataPermissionRuleHolder;
import org.springframework.stereotype.Component;

@Component
@DataPermission(excludeResources = { "class" })
public class TestServiceImpl implements TestService {

	@Override
	@DataPermission(excludeResources = { "order" })
	public DataPermissionRule methodA() {
		return DataPermissionRuleHolder.peek();
	}

	@Override
	public DataPermissionRule methodB() {
		return DataPermissionRuleHolder.peek();
	}

	@Override
	public DataPermissionRule methodC() {
		return DataPermissionRuleHolder.peek();
	}

}
