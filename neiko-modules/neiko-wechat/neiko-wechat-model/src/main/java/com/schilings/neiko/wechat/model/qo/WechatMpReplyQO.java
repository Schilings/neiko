package com.schilings.neiko.wechat.model.qo;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

/**
 *
 * <p>
 * 微信公众号自动回复多条件查询PO
 * </p>
 *
 * @author Schilings
 */
@Data
@Schema(title = "微信公众号自动回复查询对象")
@ParameterObject
public class WechatMpReplyQO {

	/**
	 * 类型（1、关注时回复；2、消息回复；3、关键词回复）
	 */
	@Parameter(description = "类型（1、关注时回复；2、消息回复；3、关键词回复）")
	private Integer type;

	/**
	 * 关键词
	 */
	@Parameter(description = "关键词")
	private String reqKey;

	/**
	 * 请求消息类型（text：文本；image：图片；voice：语音；video：视频；shortvideo：小视频；location：地理位置）
	 */
	@Parameter(description = "请求消息类型")
	private String reqType;

	/**
	 * 回复消息类型（text：文本；image：图片；voice：语音；video：视频；music：音乐；news：图文）
	 */
	@Parameter(description = "回复消息类型")
	private String repType;

}
