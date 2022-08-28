package com.schilings.neiko.system.model.dto;

import com.schilings.neiko.common.core.desensitize.annotation.JsonRegexDesensitize;
import com.schilings.neiko.common.core.desensitize.enums.RegexDesensitizationTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户密码传输DTO，字段序列化时忽略，防止记录
 */
@Data
@Schema(title = "系统用户密码传输实体")
public class SysUserPassDTO {

	/**
	 * 前端传入密码
	 */
	@NotBlank(message = "The password cannot be empty!")
	@JsonRegexDesensitize(type = RegexDesensitizationTypeEnum.ENCRYPTED_PASSWORD)
	@Schema(title = "前端输入密码")
	private String pass;

	/**
	 * 前端确认密码
	 */
	@NotBlank(message = "The confirm password cannot be empty!")
	@JsonRegexDesensitize(type = RegexDesensitizationTypeEnum.ENCRYPTED_PASSWORD)
	@Schema(title = "前端确认密码")
	private String confirmPass;

}
