package com.schilings.neiko.common.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

/**
 * <pre>{@code
 *
 * }
 * <p>分页结果封装类</p>
 * </pre>
 *
 * @author Schilings
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(title = "分页返回结果")
public class PageResult<T> {

	/**
	 * 返回的数据
	 */
	@Schema(title = "分页数据")
	private List<T> records = Collections.emptyList();

	/**
	 * 数据总数
	 */
	@Schema(title = "数据总量")
	private Long total = 0L;

	@Schema(title = "当前页数")
	private Long page = 1L;

	@Schema(title = "总页数")
	private Long pages = 1L;

	@Schema(title = "每页显示条数")
	private Long size = 10L;

}
