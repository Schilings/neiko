package com.schilings.neiko.log.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.schilings.neiko.log.enums.LoginEventTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 *
 * <p>
 * 登录日志表
 * </p>
 *
 * @author Schilings
 */
@Data
@Accessors(chain = true)
@TableName("nk_log_login_log")
@Schema(title = "登陆日志")
public class LoginLog {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId
	@Column(comment = "ID 编号")
	@Schema(title = "编号")
	private Long id;

	/**
	 * 追踪ID
	 */
	@Column(comment = "追踪ID")
	@Schema(title = "追踪ID")
	private String traceId;

	/**
	 * 客户端ID
	 */
	@Column(comment = "客户端ID")
	@Schema(title = "客户端ID")
	private Long clientId;

	/**
	 * 用户名
	 */
	@Column(comment = "用户名")
	@Schema(title = "用户名")
	private String username;

	/**
	 * 操作信息
	 */
	@Column(comment = "登陆IP")
	@Schema(title = "登陆IP")
	private String ip;

	/**
	 * 操作系统
	 */
	@Column(comment = "操作系统")
	@Schema(title = "操作系统")
	private String os;

	/**
	 * 状态
	 */
	@Column(comment = "状态")
	@Schema(title = "状态")
	private Integer status;

	/**
	 * 日志消息
	 */
	@Column(comment = "日志消息")
	@Schema(title = "日志消息")
	private String msg;

	/**
	 * 登陆地点 TODO IP解析工具暂时未定 IP解析工具类需要简单封装下，方便替换底层工具
	 */
	@Column(comment = "登陆地点")
	@Schema(title = "登陆地点")
	private String location;

	/**
	 * 事件类型 登陆/登出
	 * @see LoginEventTypeEnum
	 */
	@Column(comment = "事件类型")
	@Schema(title = "事件类型")
	private Integer eventType;

	/**
	 * 浏览器
	 */
	@Column(comment = "浏览器")
	@Schema(title = "浏览器")
	private String browser;

	/**
	 * 登录/登出时间
	 */
	@Column(comment = "登录/登出时间")
	@Schema(title = "登录/登出时间")
	private LocalDateTime loginTime;

	/**
	 * 创建时间
	 */
	@Column(comment = "创建时间")
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建时间")
	private LocalDateTime createTime;

}
