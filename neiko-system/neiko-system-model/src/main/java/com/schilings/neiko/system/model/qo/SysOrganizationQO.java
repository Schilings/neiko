package com.schilings.neiko.system.model.qo;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

/**
 * 组织架构 查询对象
 *
 */
@Data
@Schema(title = "组织架构查询对象")
@ParameterObject
public class SysOrganizationQO {

	private static final long serialVersionUID = 1L;

	/**
	 * 组织名称
	 */
	@Parameter(description = "组织名称")
	private String name;

}