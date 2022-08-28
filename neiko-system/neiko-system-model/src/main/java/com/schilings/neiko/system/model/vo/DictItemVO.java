package com.schilings.neiko.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

@Data
@Schema(title = "字典项VO")
public class DictItemVO {

	private static final long serialVersionUID = 1L;

	@Schema(title = "id")
	private Long id;

	/**
	 * 数据值
	 */
	@Schema(title = "数据值")
	private String value;

	/**
	 * 标签
	 */
	@Schema(title = "文本值")
	private String name;

	/**
	 * 状态
	 */
	@Schema(title = "状态", description = "1：启用 0：禁用")
	private Integer status;

	/**
	 * 附加属性值
	 */
	@Schema(title = "附加属性值")
	private Map<String, Object> attributes;

}
