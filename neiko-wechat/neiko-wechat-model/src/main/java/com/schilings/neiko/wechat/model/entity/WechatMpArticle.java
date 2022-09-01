package com.schilings.neiko.wechat.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.schilings.neiko.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * <p>微信公众号文章</p>
 * 
 * @author Schilings
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(title = "微信公众号文章")
@TableName("nk_wechat_mp_article")
public class WechatMpArticle extends LogicDeletedBaseEntity {

    private static final long serialVersionUID = 1L;

    /** 文章管理ID */
    @TableId
    @Column(comment = "ID")
    @Schema(title = "主键ID")
    private Long id;


    /** 分类id */
    @Column(comment = "分类id")
    @Schema(title = "分类id")
    private Long cid;


    /** 文章标题 */
    @Column(comment = "文章标题")
    @Schema(title = "文章标题")
    private String title;


    /** 文章作者 */
    @Column(comment = "文章作者")
    @Schema(title = "文章作者")
    private String author;


    /** 文章图片 */
    @Column(comment = "文章图片")
    @Schema(title = "文章图片")
    private String imageInput;


    /** 文章简介 */
    @Column(comment = "文章简介")
    @Schema(title = "文章简介")
    private String synopsis;



    @Column(comment = "文章详情")
    @Schema(title = "文章详情")
    private String content;


    /** 文章分享标题 */
    @Column(comment = "文章分享标题")
    @Schema(title = "文章分享标题")
    private String shareTitle;


    /** 文章分享简介 */
    @Column(comment = "文章分享简介")
    @Schema(title = "文章分享简介")
    private String shareSynopsis;


    /** 浏览次数 */
    @Column(comment = "浏览次数")
    @Schema(title = "浏览次数")
    private String visit;


    /** 排序 */
    @Column(comment = "排序")
    @Schema(title = "排序")
    private Integer sort;


    /** 原文链接 */
    @Column(comment = "原文链接")
    @Schema(title = "原文链接")
    private String url;


    /** 状态 */
    @Column(comment = "状态")
    @Schema(title = "状态")
    private Integer status;



}
