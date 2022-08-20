package com.schilings.neiko.system.model.qo;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

/**
 * 
 * <p>系统字典表条件查询VO</p>
 * 
 * @author Schilings
*/
@Data
@Schema(title = "字典表查询对象")
@ParameterObject
public class SysDictQO {

    private static final long serialVersionUID = 1L;
    
    /**
     * 字典标识
     */
    @Parameter(description = "字典标识")
    private String code;

    /**
     * 字典名称
     */
    @Parameter(description = "字典名称")
    private String title;

}
