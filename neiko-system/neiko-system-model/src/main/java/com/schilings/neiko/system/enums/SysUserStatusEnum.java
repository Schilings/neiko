package com.schilings.neiko.system.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SysUserStatusEnum {

    /**
     * 状态(1-正常,2-冻结)
     */
    NORMAL(1),
    FREEZE(2);
    
    private Integer status;
    
    
}
