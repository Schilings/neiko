package com.schilings.neiko.log.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

/**
 *
 * <p>
 * 操作日志表Excel导出VO
 * </p>
 *
 * @author Schilings
 */
@Data
@Schema(title = "操作日志表Excel导出VO")
public class OperationLogExcelVO {

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
	 * 日志消息
	 */
	@Schema(title = "日志消息")
	@ExcelProperty({ "日志消息" })
	private String msg;

	/**
	 * 访问IP地址
	 */
	@Schema(title = "访问IP地址")
	@ExcelProperty({ "访问IP地址" })
	private String ip;

	/**
	 * 用户代理
	 */
	@Schema(title = "用户代理")
	@ExcelProperty({ "用户代理" })
	private String userAgent;

	/**
	 * 请求URI
	 */
	@Schema(title = "请求URI")
	@ExcelProperty({ "请求URI" })
	private String uri;

	/**
	 * 请求方法
	 */
	@Schema(title = "请求方法")
	@ExcelProperty({ "请求方法" })
	private String method;

	/**
	 * 操作提交的数据
	 */
	@Schema(title = "操作提交的数据")
	@ExcelProperty({ "操作提交的数据" })
	private String params;

	/**
	 * 操作状态
	 */
	@Schema(title = "操作状态")
	@ExcelProperty({ "操作状态" })
	private Integer status;

	/**
	 * 操作类型
	 */
	@Schema(title = "操作类型")
	@ExcelProperty({ "操作类型" })
	private Integer type;

	/**
	 * 执行时长
	 */
	@Schema(title = "执行时长")
	@ExcelProperty({ "执行时长" })
	private Long time;

	/**
	 * 创建者
	 */
	@Schema(title = "创建者")
	@ExcelProperty({ "创建者" })
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private String operator;

	/**
	 * 创建时间
	 */
	@Schema(title = "创建时间")
	@ExcelProperty({ "创建时间" })
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private LocalDateTime createTime;

}
