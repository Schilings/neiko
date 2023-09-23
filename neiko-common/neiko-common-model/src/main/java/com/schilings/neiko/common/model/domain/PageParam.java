package com.schilings.neiko.common.model.domain;

import com.schilings.neiko.common.model.constants.PageableConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>{@code
 *
 * }
 * <p>分页查询参数请求类</p>
 * </pre>
 *
 * @author Schilings
 */
@Data
@Schema(title = "分页查询参数")
public class PageParam {

	/**
	 * 当前页
	 */
	@Schema(title = "当前页码", description = "从 1 开始", defaultValue = "1", example = "1")
	@Min(value = 1, message = "当前页不能小于 1")
	private long page = 1;

	/**
	 * 页面大小
	 */
	@Schema(title = "每页显示条数", description = "最大值为系统设置，默认 100", defaultValue = "10")
	@Min(value = 1, message = "每页显示条数不能小于1")
	private long size = 10;

	/**
	 * 排序字段载体
	 * <p>
	 * sort=field1,asc
	 * </p>
	 * <p>
	 * sort=field2,desc
	 * </p>
	 */
	@Schema(title = "排序规则", defaultValue = "field1,asc", example = "createTime,desc")
	@Valid
	private List<Sort> sort = new ArrayList<>();

	@Schema(title = "排序元素载体")
	@Getter
	@Setter
	public static class Sort {

		@Schema(title = "排序字段", example = "id")
		@Pattern(regexp = PageableConstants.SORT_FILED_REGEX, message = "排序字段格式非法")
		private String field;

		@Schema(title = "是否正序排序", example = "false")
		private boolean asc;

	}

	public void addSort(String field, boolean asc) {
		Sort sort = new Sort();
		sort.setField(field);
		sort.setAsc(asc);
		this.sort.add(sort);
	}

}
