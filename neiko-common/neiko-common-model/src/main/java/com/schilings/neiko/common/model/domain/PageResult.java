package com.schilings.neiko.common.model.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

/**
 * <pre>{@code
 *      
 * }
 * <p>分页结果封装类</p>
 * </pre>
 * @author Schilings
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PageResult<T> {

    /**
     * 返回的数据
     */
    private List<T> data = Collections.emptyList();

    /**
     * 数据总数
     */
    private Long total = 0L;
    
}
