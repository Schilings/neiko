package com.schilings.neiko.admin.datascope.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.schilings.neiko.admin.datascope.component.UserDataScope;

public class DataScopeJackson2Module extends SimpleModule {

	public DataScopeJackson2Module() {
		super(DataScopeJackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
	}

	@Override
	public void setupModule(SetupContext context) {
		context.setMixInAnnotations(UserDataScope.class, UserDataScopeMixin.class);
	}

}
