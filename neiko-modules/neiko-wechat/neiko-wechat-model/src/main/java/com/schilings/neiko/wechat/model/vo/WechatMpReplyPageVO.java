package com.schilings.neiko.wechat.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "微信关注自动回复分页VO")
public class WechatMpReplyPageVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 类型（1、关注时回复；2、消息回复；3、关键词回复）
	 */
	private Integer type;

	/**
	 * 关键词
	 */
	private String reqKey;

	/**
	 * 请求消息类型（text：文本；image：图片；voice：语音；video：视频；shortvideo：小视频；location：地理位置）
	 */
	private String reqType;

	/**
	 * 回复消息类型（text：文本；image：图片；voice：语音；video：视频；music：音乐；news：图文）
	 */
	private String repType;

	/**
	 * 回复类型文本匹配类型（1、全匹配，2、半匹配）
	 */
	private Integer repMate;

	/**
	 * 回复类型文本保存文字
	 */
	private String repContent;

	/**
	 * 回复的素材名、视频和音乐的标题
	 */
	private String repName;

	/**
	 * 回复类型imge、voice、news、video的mediaID或音乐缩略图的媒体id
	 */
	private String repMediaId;

	/**
	 * 视频和音乐的描述
	 */
	private String repDesc;

	/**
	 * 链接
	 */
	private String repUrl;

	/**
	 * 高质量链接
	 */
	private String repHqUrl;

	/**
	 * 缩略图的媒体id
	 */
	private String repThumbMediaId;

	/**
	 * 缩略图url
	 */
	private String repThumbUrl;

	/**
	 * 图文消息的内容
	 */
	private String content;

}
