package com.schilings.neiko.notify.model.qo;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

/**
 *
 * <p>
 * 公告信息 多条件查询QO
 * </p>
 *
 * @author Schilings
 */
@Data
@ParameterObject
@Schema(title = "公告信息查询对象")
public class AnnouncementQO {

	private static final long serialVersionUID = 1L;

	/**
	 * 标题
	 */
	@Parameter(description = "标题")
	private String title;

	/**
	 * 接收人筛选方式
	 */
	@Parameter(description = "接收人筛选方式")
	private Integer recipientFilterType;

	@Parameter(description = "状态")
	private Integer[] status;

}
