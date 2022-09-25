package com.schilings.neiko.extend.openapi.pageable;


import com.schilings.neiko.common.model.constants.PageableConstants;
import com.schilings.neiko.common.model.domain.PageParam;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 前端交互请求入参模型，将被转换为 PageParam 对象
 *
 * @see PageParam
 */
@Valid
@ParameterObject
@Getter
@Setter
@Schema(title = "分页查询入参")
public class PageableRequest {

    @Parameter(description = "当前页码, 从 1 开始", schema = @Schema(minimum = "1", defaultValue = "1", example = "1"))
    @Min(value = 1, message = "当前页不能小于 1")
    private long page = 1;

    @Parameter(description = "每页显示条数, 最大值为 100",
            schema = @Schema(title = "每页显示条数", description = "最大值为系统设置，默认 100", minimum = "1", defaultValue = "10",
                    example = "10"))
    @Min(value = 1, message = "每页显示条数不能小于1")
    private long size;

    @Parameter(description = "排序规则，格式为：property(,asc|desc)。" + "默认为升序。" + "支持传入多个排序字段。",
            array = @ArraySchema(
                    schema = @Schema(type = "string", pattern = PageableConstants.SORT_REGEX, example = "id,desc")))
    @Pattern(regexp = PageableConstants.SORT_REGEX, message = "排序字段格式非法")
    private List<String> sort;
}
