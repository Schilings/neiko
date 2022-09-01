package com.schilings.neiko.wechat.model.qo;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

/**
 *
 * <p>
 * 微信公众号文章多条件查询PO
 * </p>
 *
 * @author Schilings
 */
@Data
@Schema(title = "微信公众号文章查询对象")
@ParameterObject
public class WechatMpArticleQO {

    /** 文章标题 */
    @Parameter(description = "文章标题")
    private String title;


    /** 文章作者 */
    @Parameter(description = "文章作者")
    private String author;


    /** 状态 */
    @Parameter(description = "状态")
    private Integer status;

    @Parameter(description = "开始时间")
    private String startTime;

    @Parameter(description = "结束时间")
    private String endTime;
}
