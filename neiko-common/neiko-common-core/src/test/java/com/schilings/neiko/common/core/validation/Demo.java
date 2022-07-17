package com.schilings.neiko.common.core.validation;

import com.schilings.neiko.common.core.validation.annotation.ValueInEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <pre>{@code
 *      
 * }
 * <p>测试自定义校验注释</p>
 * </pre>
 * @author Schilings
*/
@Data
@AllArgsConstructor
public class Demo {

    @ValueInEnum(enumClass = StatusEnum.class, method = "fromValue")
    private int status;

    @ValueInEnum(enumClass = StatusEnum.class)
    private String statusName;
    
}
