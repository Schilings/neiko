package com.schilings.neiko.system.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * <p>用户-角色表</p>
 * 
 * @author Schilings
*/
@Data
@TableName("nk_sys_user_role")
@Schema(title = "用户角色")
public class SysUserRole {
    
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @Column(comment = "用户id")
    @Schema(title = "用户id")
    private Long userId;

    /**
     * 角色Code
     */
    @Column(comment = "角色Code")
    @Schema(title = "角色Code")
    private String roleCode;
}
