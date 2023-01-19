package com.schilings.neiko.system.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.schilings.neiko.common.core.desensitize.annotation.JsonRegexDesensitize;
import com.schilings.neiko.common.core.desensitize.enums.RegexDesensitizationTypeEnum;
import com.schilings.neiko.common.core.validation.annotation.ValueInInts;
import com.schilings.neiko.common.core.validation.group.CreateGroup;
import com.schilings.neiko.common.core.validation.group.UpdateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统用户表
 *
 */
@Data
@Schema(title = "系统用户DTO")
public class SysUserDTO {

	/**
	 * 主键id
	 */
	@NotNull(message = "userId不能为空", groups = UpdateGroup.class)
	@Schema(title = "主键id")
	private Long userId;

	/**
	 * 前端传入密码
	 */
	@NotEmpty(message = "密码不能为空", groups = CreateGroup.class)
	@JsonRegexDesensitize(type = RegexDesensitizationTypeEnum.ENCRYPTED_PASSWORD)
	@Schema(title = "前端传入密码")
	private String pass;

	/**
	 * 用户明文密码, 不参与前后端交互
	 */
	@JsonIgnore
	private String password;

	/**
	 * 登录账号
	 */
	@NotEmpty(message = "登录账号为空",groups = CreateGroup.class)
	@Schema(title = "登录账号")
	private String username;

	/**
	 * 昵称
	 */
	@NotEmpty(message = "昵称为空",groups = CreateGroup.class)
	@Schema(title = "昵称")
	private String nickname;

	/**
	 * 头像
	 */
	@Schema(title = "头像")
	private String avatar;

	/**
	 * 性别(0-默认未知,1-男,2-女)
	 */
	@ValueInInts(message = "sex性别为(0-默认未知,1-男,2-女)", value = { 0, 1, 2 },allowNull = true)
	@Schema(title = "性别(0-默认未知,1-男,2-女)")
	private Integer sex;

	/**
	 * 电子邮件
	 */
	@Schema(title = "电子邮件")
	private String email;

	/**
	 * 电话
	 */
	@Schema(title = "电话")
	private String phone;

	/**
	 * 状态(1-正常,2-冻结)
	 */
	@ValueInInts(message = "status状态为(1-正常,2-冻结)", value = { 1, 2 },allowNull = true)
	@Schema(title = "状态(1-正常,2-冻结)")
	private Integer status;

	/**
	 * 组织机构ID
	 */
	@Schema(title = "组织机构ID")
	private Long organizationId;

	/**
	 * 角色标识列表
	 */
	@Schema(title = "角色标识列表")
	private List<String> roleCodes;

}
