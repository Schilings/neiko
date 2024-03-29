package com.schilings.neiko.system.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schilings.neiko.common.core.desensitize.annotation.JsonRegexDesensitize;
import com.schilings.neiko.common.core.desensitize.annotation.JsonSlideDesensitize;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;
import static com.schilings.neiko.common.core.desensitize.enums.RegexDesensitizationTypeEnum.EMAIL;
import static com.schilings.neiko.common.core.desensitize.enums.SlideDesensitizationTypeEnum.PHONE_NUMBER;

/**
 *
 * <p>
 * 系统用户表分页查询VO
 * </p>
 *
 * @author Schilings
 */
@Data
@Schema(title = "系统用户VO")
public class SysUserPageVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@Schema(title = "用户ID")
	private Long userId;

	/**
	 * 登录账号
	 */
	@Schema(title = "登录账号")
	private String username;

	/**
	 * 昵称
	 */
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
	@Schema(title = "性别(0-默认未知,1-男,2-女)")
	private Integer sex;

	/**
	 * 电子邮件
	 */
	@Schema(title = "电子邮件")
	@JsonRegexDesensitize(type = EMAIL)
	private String email;

	/**
	 * 电话
	 */
	@Schema(title = "电话")
	@JsonSlideDesensitize(type = PHONE_NUMBER)
	private String phone;

	/**
	 * 状态(1-正常,0-冻结)
	 */
	@Schema(title = "状态(1-正常, 0-冻结)")
	private Integer status;

	@Schema(title = "用户类型：1-系统用户，2-客户用户")
	private Integer type;

	/**
	 * 组织机构ID
	 */
	@Schema(title = "组织机构ID")
	private Long organizationId;

	/**
	 * 组织机构名称
	 */
	@Schema(title = "组织机构名称")
	private String organizationName;

	/**
	 * 创建时间
	 */
	@Schema(title = "创建时间")
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@Schema(title = "更新时间")
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private LocalDateTime updateTime;

}
