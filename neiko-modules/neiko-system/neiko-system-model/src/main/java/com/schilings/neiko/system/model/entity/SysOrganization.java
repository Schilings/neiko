package com.schilings.neiko.system.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.schilings.neiko.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统组织架构
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("nk_sys_organization")
@Schema(title = "组织架构")
public class SysOrganization extends LogicDeletedBaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	@Column(comment = "ID")
	@Schema(title = "ID")
	private Long id;

	/**
	 * 组织名称
	 */
	@Column(comment = "组织名称")
	@Schema(title = "组织名称")
	private String name;

	/**
	 * 父级ID
	 */
	@Column(comment = "父级ID")
	@Schema(title = "父级ID")
	private Long parentId;

	/**
	 * 层级信息，从根节点到当前节点的最短路径，使用-分割节点ID
	 */
	@Column(comment = "层级信息，从根节点到当前节点的最短路径，使用-分割节点ID")
	@Schema(title = "层级信息，从根节点到当前节点的最短路径，使用-分割节点ID")
	private String hierarchy;

	/**
	 * 当前节点深度
	 */
	@Column(comment = "当前节点深度")
	@Schema(title = "当前节点深度")
	private Integer depth;

	/**
	 * 排序字段，由小到大
	 */
	@Column(comment = "排序字段，由小到大")
	@Schema(title = "排序字段，由小到大")
	private Integer sort;

	/**
	 * 备注
	 */
	@Column(comment = "备注")
	@Schema(title = "备注")
	private String remarks;

}
