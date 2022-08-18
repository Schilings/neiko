package com.schilings.neiko.common.core.validation;

import com.schilings.neiko.common.core.validation.annotation.Greater;
import com.schilings.neiko.common.core.validation.annotation.Less;
import com.schilings.neiko.common.core.validation.annotation.ValueInEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <pre>{@code
 *
 * }
 * <p>测试自定义校验注释</p>
 * </pre>
 *
 * @author Schilings
 */
@Data
@AllArgsConstructor
public class Demo {

	@ValueInEnum(enumClass = StatusEnum.class, method = "fromValue")
	private int status;

	@ValueInEnum(enumClass = StatusEnum.class)
	private String statusName;

	@Greater(value = 10, equal = true)
	private Integer value1;

	@Less(value = 10, equal = false)
	private Integer value2;

	private List<Object> list;

}
