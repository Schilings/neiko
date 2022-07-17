package com.schilings.neiko.common.model.domain;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * <pre>{@code
 *      
 * }
 * <p>分页查询参数请求类</p>
 * </pre>
 * @author Schilings
*/
@Data
public class PageParam {

    /**
     * 当前页
     */
    @Min(value = 1, message = "当前页不能小于 1")
    private long page = 1;

    /**
     * 页面大小
     */
    @Min(value = 1, message = "每页显示条数不能小于1")
    private long size = 10;

    /**
     * 排序规则
     */
    


}
