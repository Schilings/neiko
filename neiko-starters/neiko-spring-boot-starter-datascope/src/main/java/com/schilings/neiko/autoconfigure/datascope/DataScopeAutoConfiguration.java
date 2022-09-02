package com.schilings.neiko.autoconfigure.datascope;


import com.schilings.neiko.common.datascope.DataScope;
import com.schilings.neiko.common.datascope.advisor.DataPermissionAnnotationAdvisor;
import com.schilings.neiko.common.datascope.core.DataPermissionHandler;
import com.schilings.neiko.common.datascope.core.DefaultDataPermissionHandler;
import com.schilings.neiko.common.datascope.interceptor.DataPermissionInterceptor;
import com.schilings.neiko.common.datascope.sql.DataScopeSqlProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnBean(DataScope.class)
public class DataScopeAutoConfiguration {

	/**
	 * 数据权限注解 Advisor，用于处理数据权限的链式调用关系
	 * @return DataPermissionAnnotationAdvisor
	 */
	@Bean
	@ConditionalOnMissingBean(DataPermissionAnnotationAdvisor.class)
	public DataPermissionAnnotationAdvisor dataPermissionAnnotationAdvisor() {
		return new DataPermissionAnnotationAdvisor();
	}

	/**
	 * 数据权限处理器
	 * @param dataScopeList 需要控制的数据范围集合
	 * @return DataPermissionHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public DataPermissionHandler dataPermissionHandler(List<DataScope> dataScopeList) {
		return new DefaultDataPermissionHandler(dataScopeList);
	}

	/**
	 * mybatis 拦截器，用于拦截处理 sql
	 * @param dataPermissionHandler 数据权限处理器
	 * @return DataPermissionInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean
	public DataPermissionInterceptor dataPermissionInterceptor(DataPermissionHandler dataPermissionHandler) {
		return new DataPermissionInterceptor(new DataScopeSqlProcessor(), dataPermissionHandler);
	}


}
