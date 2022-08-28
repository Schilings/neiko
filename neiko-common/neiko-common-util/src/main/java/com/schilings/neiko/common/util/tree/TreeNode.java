package com.schilings.neiko.common.util.tree;

import java.util.List;

public interface TreeNode<I> {

	/**
	 * 获取节点id
	 * @return 树节点id
	 */
	I getId();

	/**
	 * 获取该节点的父节点id
	 * @return 父节点id
	 */
	I getParentId();

	/**
	 * 设置节点的子节点列表
	 * @param children 子节点
	 */
	<T extends TreeNode<I>> void setChildren(List<T> children);

	/**
	 * 获取所有子节点
	 * @return 子节点列表
	 */
	<T extends TreeNode<I>> List<T> getChildren();

}
