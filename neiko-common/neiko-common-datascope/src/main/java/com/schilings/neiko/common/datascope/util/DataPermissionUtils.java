package com.schilings.neiko.common.datascope.util;


import com.schilings.neiko.common.datascope.function.Task;
import com.schilings.neiko.common.datascope.core.DataPermissionRule;
import com.schilings.neiko.common.datascope.core.DataPermissionRuleHolder;

public final class DataPermissionUtils {

	private DataPermissionUtils() {

	}

	/**
	 * 使用指定的数据权限执行任务
	 * @param dataPermissionRule 当前任务执行时使用的数据权限规则
	 * @param task 待执行的动作
	 */
	public static void executeWithDataPermissionRule(DataPermissionRule dataPermissionRule, Task task) {
		DataPermissionRuleHolder.push(dataPermissionRule);
		try {
			task.perform();
		}
		finally {
			DataPermissionRuleHolder.poll();
		}
	}

}
