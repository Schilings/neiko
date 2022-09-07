package com.schilings.neiko.common.datascope.core;

import com.schilings.neiko.common.datascope.DataScope;

import java.util.List;

/**
 *
 * <p>
 * 数据权限处理器
 * </p>
 * <p>
 * {@link com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler}
 * </p>
 * <p>
 * {@link com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler}
 * </p>
 *
 * @author Schilings
 */
public interface DataPermissionHandler {

	/**
	 * 系统配置的所有的数据范围
	 * @return 数据范围集合
	 */
	List<DataScope> dataScopes();

	/**
	 * 根据权限注解过滤后的数据范围集合
	 * @param mappedStatementId Mapper方法ID
	 * @return 数据范围集合
	 */
	List<DataScope> filterDataScopes(String mappedStatementId);

	/**
	 * 是否忽略权限控制，用于及早的忽略控制，例如管理员直接放行，而不必等到DataScope中再进行过滤处理，提升效率
	 * @return boolean true: 忽略，false: 进行权限控制
	 * @param dataScopeList 当前应用的 dataScope 集合
	 * @param mappedStatementId Mapper方法ID
	 */
	boolean ignorePermissionControl(List<DataScope> dataScopeList, String mappedStatementId);

}
