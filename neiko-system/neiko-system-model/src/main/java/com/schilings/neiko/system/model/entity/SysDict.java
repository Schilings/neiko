package com.schilings.neiko.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.schilings.neiko.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * <p>系统字典表</p>
 * 
 * @author Schilings
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("nk_sys_dict")
@Schema(title = "字典表")
public class SysDict extends LogicDeletedBaseEntity {
    
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId
    @Column(comment = "主键ID")
    @Schema(title = "编号")
    private Long id;

    /**
     * 标识
     */
    @Column(comment = "标识")
    @Schema(title = "标识")
    private String code;

    /**
     * 名称
     */
    @Column(comment = "名称")
    @Schema(title = "名称")
    private String title;

    /**
     * Hash值
     */
    @Column(comment = "Hash值")
    @Schema(title = "Hash值")
    private String hashCode;

    /**
     * 数据类型
     */
    @Column(comment = "数据类型（1:Number 2:String 3:Boolean）")
    @Schema(title = "数据类型", description = "1:Number 2:String 3:Boolean")
    private Integer valueType;

    /**
     * 备注
     */
    @Column(comment = "备注")
    @Schema(title = "备注")
    private String remarks;

}
