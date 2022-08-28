package com.schilings.neiko.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 组织架构
 */
@Data
@Schema(title = "组织架构DTO")
public class SysOrganizationDTO {

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
	 * 排序字段，由小到大
	 */
	@Schema(title = "排序字段，由小到大")
	private Integer sort;

	/**
	 * 备注
	 */
	@Schema(title = "备注")
	private String remarks;

}
