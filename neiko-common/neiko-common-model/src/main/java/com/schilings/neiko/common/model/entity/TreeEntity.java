package com.schilings.neiko.common.model.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


/**
 * <pre>{@code
 *      
 * }
 * <p>有父子关系的树形基类</p>
 * </pre>
 * @author Schilings
*/
@Setter
@Getter
public abstract class TreeEntity<T> extends BaseEntity implements Serializable {

    /**
     * 父
     */
    private String parentId;
    private T parent;

    /**
     * 子
     */
    private List<T> children;
    
    
}
