package com.schilings.neiko.common.datascope.core;

import com.schilings.neiko.common.datascope.DataScope;
import com.schilings.neiko.common.datascope.holder.MappedStatementIdsWithoutDataScope;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * <p>
 * 默认的数据权限控制处理器，核心功能就是返回哪一些DataScope
 * </p>
 *
 * @author Schilings
 */
@RequiredArgsConstructor
public class DefaultDataPermissionHandler implements DataPermissionHandler {

	private final List<DataScope> dataScopes;

	/**
	 * 系统配置的所有的数据范围
	 * @return 数据范围集合
	 */
	@Override
	public List<DataScope> dataScopes() {
		return dataScopes;
	}

	/**
	 * 系统配置的所有的数据范围
	 * <p>
	 * 默认实现：Mapper方法ID 暂不起作用，通过业务配置的DataPermissionRule来进行DataScope筛选
	 * </p>
	 * @param mappedStatementId Mapper方法ID 暂不起作用
	 * @return 数据范围集合
	 */
	@Override
	public List<DataScope> filterDataScopes(String mappedStatementId) {
		if (this.dataScopes == null || this.dataScopes.isEmpty()) {
			return new ArrayList<>();
		}
		// 获取权限规则
		DataPermissionRule dataPermissionRule = DataPermissionRuleHolder.peek();
		return filterDataScopes(dataPermissionRule);
	}

	/**
	 * <p>
	 * 是否忽略权限控制
	 * </p>
	 * 若当前的 mappedStatementId 存在于 <Code>MappedStatementIdsWithoutDataScope<Code/>
	 * 中，则表示无需处理
	 * @param dataScopeList 当前需要控制的 dataScope 集合
	 * @param mappedStatementId Mapper方法ID
	 * @return always false
	 */
	@Override
	public boolean ignorePermissionControl(List<DataScope> dataScopeList, String mappedStatementId) {
		return MappedStatementIdsWithoutDataScope.onAllWithoutSet(dataScopeList, mappedStatementId);
	}

	/**
	 * 根据数据权限规则过滤出 dataScope 列表
	 * @param dataPermissionRule 数据权限规则
	 * @return List<DataScope>
	 */
	protected List<DataScope> filterDataScopes(DataPermissionRule dataPermissionRule) {
		// 如果没有配置权限规则，则使用全局规则DataScope
		if (dataPermissionRule == null) {
			return dataScopes;
		}

		// 如果配置忽略数据权限，则返回空
		if (dataPermissionRule.ignore()) {
			return new ArrayList<>();
		}

		// 当指定了只包含的资源时，只对该资源的DataScope
		if (dataPermissionRule.includeResources().length > 0) {
			Set<String> a = new HashSet<>(Arrays.asList(dataPermissionRule.includeResources()));
			return dataScopes.stream().filter(x -> a.contains(x.getResource())).collect(Collectors.toList());
		}

		// 当未指定只包含的资源，且指定了排除的资源时，则排除此部分资源的 DataScope
		if (dataPermissionRule.excludeResources().length > 0) {
			Set<String> a = new HashSet<>(Arrays.asList(dataPermissionRule.excludeResources()));
			return dataScopes.stream().filter(x -> !a.contains(x.getResource())).collect(Collectors.toList());
		}

		return dataScopes;
	}

}
