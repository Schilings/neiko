package com.schilings.neiko.log.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * <p>
 * 访问日志表Excel导出VO
 * </p>
 *
 * @author Schilings
 */
@Data
@Schema(title = "访问日志Excel导出对象")
public class AccessLogExcelVO {
    
    /**
     * 编号
     */
    @Schema(title = "编号")
    @ExcelProperty({"编号"})
    private Long id;

    /**
     * 追踪ID
     */
    @Schema(title = "追踪ID")
    @ExcelProperty({"追踪ID"})
    private String traceId;

    /**
     * 用户ID
     */
    @Schema(title = "用户ID")
    @ExcelProperty({"用户ID"})
    private Integer userId;

    /**
     * 用户名
     */
    @Schema(title = "用户名")
    @ExcelProperty({"用户名"})
    private String username;

    /**
     * 访问IP地址
     */
    @Schema(title = "访问IP地址")
    @ExcelProperty({"访问IP地址"})
    private String ip;

    /**
     * 用户代理
     */
    @Schema(title = "用户代理")
    @ExcelProperty({"用户代理"})
    private String userAgent;

    /**
     * 请求URI
     */
    @Schema(title = "请求URI")
    @ExcelProperty({"请求URI"})
    private String uri;

    /**
     * 请求映射地址
     */
    @Schema(title = "请求映射地址")
    @ExcelProperty({"请求映射地址"})
    private String matchingPattern;

    /**
     * 操作方式
     */
    @Schema(title = "操作方式")
    @ExcelProperty({"操作方式"})
    private String method;

    /**
     * 请求参数
     */
    @Schema(title = "请求参数")
    @ExcelProperty({"请求参数"})
    private String reqParams;

    /**
     * 请求body
     */
    @Schema(title = "请求body")
    @ExcelProperty({"请求body"})
    private String reqBody;

    /**
     * 响应状态码
     */
    @Schema(title = "响应状态码")
    @ExcelProperty({"响应状态码"})
    private Integer httpStatus;

    /**
     * 响应信息
     */
    @Schema(title = "响应信息")
    @ExcelProperty({"响应信息"})
    private String result;

    /**
     * 错误消息
     */
    @Schema(title = "错误消息")
    @ExcelProperty({"错误消息"})
    private String errorMsg;

    /**
     * 执行时长
     */
    @Schema(title = "执行时长")
    @ExcelProperty({"执行时长"})
    private Long time;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    @ExcelProperty({"创建时间"})
    private LocalDateTime createTime;

}
