package com.schilings.neiko.notify.model.qo;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

/**
 * 用户公告表 多条件查询QO
 *
 */
@Data
@Schema(title = "用户公告表查询对象")
@ParameterObject
public class UserAnnouncementQO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Parameter(description = "ID")
    private Long id;
}
