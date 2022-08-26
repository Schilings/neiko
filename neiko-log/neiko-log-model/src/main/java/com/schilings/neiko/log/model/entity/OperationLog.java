package com.schilings.neiko.log.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 *
 * <p>
 * 操作日志表
 * </p>
 *
 * @author Schilings
 */
@Data
@TableName("nk_log_operation_log")
@Accessors(chain = true)
@Schema(title = "操作日志")
public class OperationLog {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId
	@Column(comment = "主键ID 编号")
	@Schema(title = "编号")
	private Long id;

	/**
	 * 追踪ID
	 */
	@Column(comment = "追踪ID")
	@Schema(title = "追踪ID")
	private String traceId;

	/**
	 * 日志消息
	 */
	@Column(comment = "日志消息")
	@Schema(title = "日志消息")
	private String msg;

	/**
	 * 访问IP地址
	 */
	@Column(comment = "访问IP地址")
	@Schema(title = "访问IP地址")
	private String ip;

	/**
	 * 用户代理
	 */
	@Column(comment = "用户代理")
	@Schema(title = "用户代理")
	private String userAgent;

	/**
	 * 请求URI
	 */
	@Column(comment = "请求URI")
	@Schema(title = "请求URI")
	private String uri;

	/**
	 * 请求方法
	 */
	@Column(comment = "请求方法")
	@Schema(title = "请求方法")
	private String method;

	/**
	 * 操作提交的数据
	 */
	@Column(comment = "操作提交的数据")
	@Schema(title = "操作提交的数据")
	private String params;

	/**
	 * 操作状态
	 */
	@Column(comment = "操作状态")
	@Schema(title = "操作状态")
	private Integer status;

	/**
	 * 操作类型
	 */
	@Column(comment = "操作类型")
	@Schema(title = "操作类型")
	private Integer type;

	/**
	 * 执行时长
	 */
	@Column(comment = "执行时长")
	@Schema(title = "执行时长")
	private Long time;

	/**
	 * 操作结果
	 */
	@Column(comment = "操作结果")
	@Schema(title = "操作结果")
	private String result;

	/**
	 * 创建者
	 */
	@Column(comment = "创建者")
	@Schema(title = "创建者")
	private String operator;

	/**
	 * 创建时间
	 */
	@Column(comment = "创建时间")
	@Schema(title = "创建时间")
	private LocalDateTime createTime;

}
