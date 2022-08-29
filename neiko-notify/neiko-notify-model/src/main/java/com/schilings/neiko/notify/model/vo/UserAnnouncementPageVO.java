package com.schilings.neiko.notify.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户公告表 分页查询结果VO
 */
@Data
@Schema(title = "用户公告分页VO")
public class UserAnnouncementPageVO {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@Schema(title = "ID")
	private Long id;

	/**
	 * 公告id
	 */
	@Schema(title = "公告id")
	private Long announcementId;

	/**
	 * 用户ID
	 */
	@Schema(title = "用户ID")
	private Long userId;

	/**
	 * 状态，已读(1)|未读(0)
	 */
	@Schema(title = "状态，已读(1)|未读(0)")
	private Integer state;

	/**
	 * 阅读时间
	 */
	@Schema(title = "阅读时间")
	private LocalDateTime readTime;

	/**
	 * 拉取时间
	 */
	@Schema(title = "拉取时间")
	private LocalDateTime createTime;

}