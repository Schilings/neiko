package com.schilings.neiko.log.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.schilings.neiko.log.enums.LoginEventTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * <p>
 * 登录日志表Excel导出VO
 * </p>
 *
 * @author Schilings
 */
@Data
@Schema(title = "登录日志表Excel导出VO")
public class LoginLogExcelVO {

	/**
	 * 编号
	 */
	@Schema(title = "编号")
	@ExcelProperty({ "编号" })
	private Long id;

	/**
	 * 追踪ID
	 */
	@Schema(title = "追踪ID")
	@ExcelProperty({ "追踪ID" })
	private String traceId;

	/**
	 * 客户端ID
	 */
	@Schema(title = "客户端ID")
	@ExcelProperty({ "客户端ID" })
	private Long clienId;

	/**
	 * 用户名
	 */
	@Schema(title = "用户名")
	@ExcelProperty({ "用户名" })
	private String username;

	/**
	 * 操作信息
	 */
	@Schema(title = "操作信息")
	@ExcelProperty({ "操作信息" })
	private String ip;

	/**
	 * 操作系统
	 */
	@Schema(title = "操作系统")
	@ExcelProperty({ "操作系统" })
	private String os;

	/**
	 * 状态
	 */
	@Schema(title = "状态")
	@ExcelProperty({ "状态" })
	private Integer status;

	/**
	 * 日志消息
	 */
	@Schema(title = "日志消息")
	@ExcelProperty({ "日志消息" })
	private String msg;

	/**
	 * 登陆地点
	 */
	@Schema(title = "登陆地点")
	@ExcelProperty({ "登陆地点" })
	private String location;

	/**
	 * 事件类型 登陆/登出
	 * @see LoginEventTypeEnum
	 */
	@Schema(title = "事件类型")
	@ExcelProperty({ "事件类型" })
	private Integer eventType;

	/**
	 * 浏览器
	 */
	@Schema(title = "浏览器")
	@ExcelProperty({ "浏览器" })
	private String browser;

	/**
	 * 登录/登出时间
	 */
	@Schema(title = "登录/登出时间")
	@ExcelProperty({ "登录/登出时间" })
	private LocalDateTime loginTime;

	/**
	 * 创建时间
	 */
	@Schema(title = "创建时间")
	@ExcelProperty({ "创建时间" })
	private LocalDateTime createTime;

}
