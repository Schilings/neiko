package com.schilings.neiko.system.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.schilings.neiko.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;

import java.util.Map;

/**
 *
 * <p>
 * 字典项
 * </p>
 *
 * @author Schilings
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "nk_sys_dict_item", autoResultMap = true)
@Schema(title = "字典项")
public class SysDictItem extends LogicDeletedBaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Column(comment = "主键ID")
	@Schema(title = "主键ID")
	private Long id;

	/**
	 * 字典标识
	 */
	@Column(comment = "字典标识")
	@Schema(title = "字典标识")
	private String dictCode;

	/**
	 * 数据值
	 */
	@Column(comment = "数据值")
	@Schema(title = "数据值")
	private String value;

	/**
	 * 文本值
	 */
	@Column(comment = "文本值")
	@Schema(title = "文本值")
	private String name;

	/**
	 * 状态
	 */
	@Column(comment = "状态(1：启用 0：禁用)")
	@Schema(title = "状态", description = "1：启用 0：禁用")
	private Integer status;

	/**
	 * 附加属性值
	 */
	@TableField(jdbcType = JdbcType.VARCHAR, typeHandler = JacksonTypeHandler.class)
	@Column(comment = "附加属性值", type = MySqlTypeConstant.JSON)
	@Schema(title = "附加属性值")
	private Map<String, Object> attributes;

	/**
	 * 排序（升序）
	 */
	@Column(comment = "排序（升序")
	@Schema(title = "排序（升序）")
	private Integer sort;

	/**
	 * 备注
	 */
	@Column(comment = "备注")
	@Schema(title = "备注")
	private String remarks;

}
