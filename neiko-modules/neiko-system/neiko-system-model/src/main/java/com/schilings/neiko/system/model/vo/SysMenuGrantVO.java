package com.schilings.neiko.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 菜单权限授权对象
 */
@Data
@Schema(title = "菜单权限授权对象")
public class SysMenuGrantVO {

	private static final long serialVersionUID = 1L;

	/**
	 * 菜单ID
	 */
	@Schema(title = "菜单ID")
	private Long id;

	/**
	 * 父级ID
	 */
	@Schema(title = "父级ID")
	private Long parentId;

	@Schema(title = "菜单名称")
	private String title;

	/**
	 * 菜单类型 （0目录，1菜单，2按钮）
	 */
	@Schema(title = "菜单类型 （0目录，1菜单，2按钮）")
	private Integer type;

}