package com.schilings.neiko.system.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("nk_sys_role_menu")
@Schema(title = "角色菜单")
public class SysRoleMenu {


    private static final long serialVersionUID = 1L;

    public SysRoleMenu() {
    }

    public SysRoleMenu(String roleCode, Long menuId) {
        this.roleCode = roleCode;
        this.menuId = menuId;
    }

    @TableId(type = IdType.AUTO)
    @Column(comment = "ID")
    private Long id;

    /**
     * 角色 Code
     */
    @Column(comment = "角色 Code")
    @Schema(title = "角色 Code")
    private String roleCode;

    /**
     * 权限ID
     */
    @Column(comment = "菜单ID")
    @Schema(title = "菜单id")
    private Long menuId;
}
