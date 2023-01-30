package com.schilings.neiko.system.model.vo;

import com.schilings.neiko.common.util.tree.TreeNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

/**
 * 组织架构
 *
 * @author hccake 2020-09-23 12:09:43
 */
@Data
@Schema(title = "组织架构")
public class SysOrganizationTree implements TreeNode<Long> {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@Schema(title = "ID")
	private Long id;

	/**
	 * 组织名称
	 */
	@Schema(title = "组织名称")
	private String name;

	/**
	 * 父级ID
	 */
	@Schema(title = "父级ID")
	private Long parentId;

	/**
	 * 层级信息，从根节点到当前节点的最短路径，使用-分割节点ID
	 */
	@Schema(title = "层级信息，从根节点到当前节点的最短路径，使用-分割节点ID")
	private String hierarchy;

	/**
	 * 当前节点深度
	 */
	@Schema(title = "当前节点深度")
	private Integer depth;

	/**
	 * 排序字段，由小到大
	 */
	@Schema(title = "排序字段，由小到大")
	private Integer sort;

	/**
	 * 描述信息
	 */
	@Schema(title = "描述信息")
	private String remarks;

	/**
	 * 创建时间
	 */
	@Schema(title = "创建时间")
	@DateTimeFormat(pattern = NORM_DATETIME_PATTERN)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@Schema(title = "更新时间")
	@DateTimeFormat(pattern = NORM_DATETIME_PATTERN)
	private LocalDateTime updateTime;

	/**
	 * 下级组织
	 */
	@Schema(title = "下级组织")
	List<SysOrganizationTree> children = new ArrayList<>();

	/**
	 * 设置节点的子节点列表
	 * @param children 子节点
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T extends TreeNode<Long>> void setChildren(List<T> children) {
		this.children = (List<SysOrganizationTree>) children;
	}

}