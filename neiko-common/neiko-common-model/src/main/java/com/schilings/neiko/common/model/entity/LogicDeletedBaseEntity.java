package com.schilings.neiko.common.model.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 逻辑删除的实体类基类 借鉴作者：↓
 *
 * @author hccake
 */
@Getter
@Setter
public abstract class LogicDeletedBaseEntity extends BaseEntity {

	/**
	 * 逻辑删除标识，已删除: 删除时间戳，未删除: 0
	 */
	@TableField
	@Schema(title = "已删除: 删除时间戳，未删除: 0")
	private Long deleted;

}
