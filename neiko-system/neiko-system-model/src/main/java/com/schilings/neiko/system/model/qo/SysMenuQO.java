package com.schilings.neiko.system.model.qo;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

/**
 * 
 * <p>系统菜单权限表条件查询VO</p>
 * 
 * @author Schilings
*/
@Data
@Schema(title = "菜单权限查询对象")
@ParameterObject
public class SysMenuQO {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @Parameter(description = "菜单ID")
    private Long id;

    /**
     * 菜单名称
     */
    @Parameter(description = "菜单名称")
    private String title;

    /**
     * 授权标识
     */
    @Parameter(description = "授权标识")
    private String permission;

    /**
     * 路由地址
     */
    @Parameter(description = "路由地址")
    private String path;

}