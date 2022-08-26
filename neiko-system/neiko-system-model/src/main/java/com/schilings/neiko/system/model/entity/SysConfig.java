package com.schilings.neiko.system.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.schilings.neiko.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * <p>
 * 系统配置表
 * </p>
 *
 * @author Schilings
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(title = "基础配置")
@TableName("nk_sys_config")
public class SysConfig extends LogicDeletedBaseEntity {

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Column(comment = "主键ID")
	@Schema(title = "主键ID")
	private Long id;

	/**
	 * 配置名称
	 */
	@Column(comment = "配置名称")
	@Schema(title = "配置名称")
	private String name;

	/**
	 * 配置在缓存中的key名
	 */
	@Column(comment = "配置在缓存中的key名")
	@Schema(title = "配置在缓存中的key名")
	private String confKey;

	/**
	 * 配置值
	 */
	@Column(comment = "配置值")
	@Schema(title = "配置值")
	private String confValue;

	/**
	 * 分类
	 */
	@Column(comment = "分类")
	@Schema(title = "分类")
	private String category;

	/**
	 * 备注
	 */
	@Column(comment = "备注")
	@Schema(title = "备注")
	private String remarks;

}
