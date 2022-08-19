package com.schilings.neiko.system.model.qo;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 
 * <p>角色绑定用户查询对象</p>
 * 
 * @author Schilings
*/
@Data
@Schema(title = "角色绑定用户查询对象")
public class RoleBindUserQO {

    @NotNull(message = "角色标识不能为空！")
    @Parameter(description = "角色标识")
    private String roleCode;

    @Parameter(description = "用户ID")
    private Long userId;

    @Parameter(description = "用户名")
    private String username;

    @Parameter(description = "组织ID")
    private Integer organizationId;

}