package com.schilings.neiko.common.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <pre>{@code
 *
 * }
 * <p>基础的实体基类</p>
 * </pre>
 *
 * @author Schilings
 */
@Getter
@Setter
public abstract class BaseEntity implements Serializable {

	/**
	 * 创建者
	 */
	// @TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建者")
	private Integer createBy;

	/**
	 * 更新者
	 */
	// @TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "更新者")
	private Integer updateBy;

	/**
	 * 创建时间
	 */
	// @TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	// @TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改时间")
	private LocalDateTime updateTime;

}