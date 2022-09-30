package com.schilings.neiko.log.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * <p>
 * 操作日志表分页查询VO
 * </p>
 *
 * @author Schilings
 */
@Data
@Schema(title = "操作日志视图对象")
public class OperationLogPageVO {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@Schema(title = "编号")
	private Long id;

	/**
	 * 追踪ID
	 */
	@Schema(title = "追踪ID")
	private String traceId;

	/**
	 * 日志消息
	 */
	@Schema(title = "日志消息")
	private String msg;

	/**
	 * 访问IP地址
	 */
	@Schema(title = "访问IP地址")
	private String ip;

	/**
	 * 用户代理
	 */
	@Schema(title = "用户代理")
	private String userAgent;

	/**
	 * 请求URI
	 */
	@Schema(title = "请求URI")
	private String uri;

	/**
	 * 请求方法
	 */
	@Schema(title = "请求方法")
	private String method;

	/**
	 * 操作提交的数据
	 */
	@Schema(title = "操作提交的数据")
	private String params;

	/**
	 * 操作状态
	 */
	@Schema(title = "操作状态")
	private Integer status;

	/**
	 * 操作类型
	 */
	@Schema(title = "操作类型")
	private Integer type;

	/**
	 * 执行时长
	 */
	@Schema(title = "执行时长")
	private Long time;

	/**
	 * 创建者
	 */
	@Schema(title = "创建者")
	private String operator;

	/**
	 * 创建时间
	 */
	@Schema(title = "创建时间")
	private LocalDateTime createTime;

}
