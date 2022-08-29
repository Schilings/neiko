package com.schilings.neiko.log.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 *
 * <p>
 * 访问日志表
 * </p>
 *
 * @author Schilings
 */
@Data
@TableName("nk_log_access_log")
@Accessors(chain = true)
@Schema(title = "后台访问日志")
public class AccessLog {

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
	 * 用户ID
	 */
	@Column(comment = "用户ID")
	@Schema(title = "用户ID")
	private Long userId;

	/**
	 * 用户名
	 */
	@Column(comment = "用户名")
	@Schema(title = "用户名")
	private String username;

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
	 * 请求映射地址
	 */
	@Column(comment = "请求映射地址")
	@Schema(title = "请求映射地址")
	private String matchingPattern;

	/**
	 * 操作方式
	 */
	@Column(comment = "操作方式")
	@Schema(title = "操作方式")
	private String method;

	/**
	 * 请求参数
	 */
	@Column(comment = "请求参数",type = MySqlTypeConstant.TEXT)
	@Schema(title = "请求参数")
	private String reqParams;

	/**
	 * 请求body
	 */
	@Column(comment = "请求body",type = MySqlTypeConstant.TEXT)
	@Schema(title = "请求body")
	private String reqBody;

	/**
	 * 响应状态码
	 */
	@Column(comment = "响应状态码")
	@Schema(title = "响应状态码")
	private Integer httpStatus;

	/**
	 * 响应信息
	 */
	@Column(comment = "响应信息", type = MySqlTypeConstant.TEXT)
	@Schema(title = "响应信息")
	private String result;

	/**
	 * 错误消息
	 */
	@Column(comment = "错误消息",type = MySqlTypeConstant.TEXT)
	@Schema(title = "错误消息")
	private String errorMsg;

	/**
	 * 执行时长
	 */
	@Column(comment = "执行时长")
	@Schema(title = "执行时长")
	private Long time;

	/**
	 * 创建时间
	 */
	@Column(comment = "创建时间")
	@Schema(title = "创建时间")
	private LocalDateTime createTime;

}
