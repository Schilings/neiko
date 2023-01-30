package com.schilings.neiko.common.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
	@TableLogic//作用于select与delete,值可无、会自动获取全局配置
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "已删除: 删除时间戳，未删除: 0")
	private Long deleted;

}
