package com.schilings.neiko.common.model.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

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
     * 排序字段载体
     */
    @Valid
    private List<Sort> sorts = new ArrayList<>();

    /**
     * 排序规则
     */
    @Getter
    @Setter
    public static class Sort {

        public static final String DESC = "DESC";
        public static final String ASC = "ASC";

        /**
         * 排序字段
         */
        private String field;

        /**
         * 排序规则
         */
        private String rule;
    }
    


}
