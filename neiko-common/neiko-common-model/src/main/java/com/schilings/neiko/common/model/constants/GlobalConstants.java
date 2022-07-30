package com.schilings.neiko.common.model.constants;

public interface GlobalConstants {

	/**
	 * 未被逻辑删除的标识，即有效数据标识 逻辑删除标识，普通情况下可以使用 1 标识删除，0 标识存活 但在有唯一索引的情况下，会导致索引冲突，所以用 0 标识存活，
	 * 已删除数据记录为删除时间戳
	 */
	Long NOT_DELETED_FLAG = 0L;

	/**
	 * 生产环境
	 */
	String ENV_PROD = "prod";

	/**
	 * 树根节点ID
	 */
	Integer TREE_ROOT_ID = 0;

}
