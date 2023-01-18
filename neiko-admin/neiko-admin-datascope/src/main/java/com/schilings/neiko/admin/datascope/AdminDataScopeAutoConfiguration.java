package com.schilings.neiko.admin.datascope;

import com.schilings.neiko.admin.datascope.component.AdminDefaultDataScope;
import com.schilings.neiko.admin.datascope.component.DefaultUserDataScopeProcessor;
import com.schilings.neiko.admin.datascope.component.UserDataScopeProcessor;
import com.schilings.neiko.admin.datascope.coordinator.DataScopeUserInfoCoordinator;
import com.schilings.neiko.system.service.SysOrganizationService;
import com.schilings.neiko.system.service.SysRoleService;
import com.schilings.neiko.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 *
 * <p>
 * </p>
 *
 * @author Schilings
 */
@AutoConfiguration
@RequiredArgsConstructor
public class AdminDataScopeAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public AdminDefaultDataScope adminDefaultDataScope() {
		return new AdminDefaultDataScope();
	}

	@Bean
	@ConditionalOnMissingBean
	public DefaultUserDataScopeProcessor defaultUserDataScopeProcessor(SysOrganizationService sysOrganizationService,
			SysUserService sysUserService, SysRoleService sysRoleService) {
		return new DefaultUserDataScopeProcessor(sysOrganizationService, sysUserService, sysRoleService);
	}

	@Bean
	@ConditionalOnMissingBean
	public DataScopeUserInfoCoordinator userInfoCoordinator(UserDataScopeProcessor userDataScopeProcessor) {
		return new DataScopeUserInfoCoordinator(userDataScopeProcessor);
	}

}
