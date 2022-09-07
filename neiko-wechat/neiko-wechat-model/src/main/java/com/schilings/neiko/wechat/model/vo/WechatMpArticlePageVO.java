package com.schilings.neiko.wechat.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(title = "微信公众号文章分页VO")
public class WechatMpArticlePageVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 文章管理ID */
	private Long id;

	/** 分类id */
	private String cid;

	/** 文章标题 */
	private String title;

	/** 文章作者 */
	private String author;

	/** 文章图片 */
	private String imageInput;

	/** 文章简介 */
	private String synopsis;

	/**
	 * 文章内容
	 */
	private String content;

	/** 文章分享标题 */
	private String shareTitle;

	/** 文章分享简介 */
	private String shareSynopsis;

	/** 浏览次数 */
	private String visit;

	/** 排序 */
	private Integer sort;

	/** 原文链接 */
	private String url;

	/** 状态 */
	private Integer status;

	/**
	 * 创建时间
	 */
	@Schema(title = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@Schema(title = "更新时间")
	private LocalDateTime updateTime;

}
