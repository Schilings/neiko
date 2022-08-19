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
 * <p>系统用户表</p>
 * 
 * @author Schilings
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("nk_sys_user")
@Schema(title = "系统用户表")
public class SysUser extends LogicDeletedBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId
    @Column(comment = "主键id")
    @Schema(title = "用户ID")
    private Long userId;

    /**
     * 登录账号
     */

    @Column(comment = "登录账号")
    @Schema(title = "登录账号")
    private String username;

    /**
     * 昵称
     */

    @Column(comment = "昵称")
    @Schema(title = "昵称")
    private String nickname;

    /**
     * 密码
     */

    @Column(comment = "密码")
    @Schema(title = "密码")
    private String password;

    /**
     * md5密码盐
     */

    @Column(comment = "md5密码盐")
    @Schema(title = "md5密码盐")
    private String salt;

    /**
     * 头像
     */

    @Column(comment = "头像")
    @Schema(title = "头像")
    private String avatar;

    /**
     * 性别(0-默认未知,1-男,2-女)
     */

    @Column(comment = "性别(0-默认未知,1-男,2-女)")
    @Schema(title = "性别(0-默认未知,1-男,2-女)")
    private Integer sex;

    /**
     * 电子邮件
     */
    @Column(comment = "电子邮件")
    @Schema(title = "电子邮件")
    private String email;

    /**
     * 电话
     */

    @Column(comment = "电话")
    @Schema(title = "电话")
    private String phone;

    /**
     * 状态(1-正常,0-冻结)
     */

    @Column(comment = "状态(1-正常, 0-冻结)")
    @Schema(title = "状态(1-正常, 0-冻结)")
    private Integer status;

    /**
     * 组织机构ID
     */
    
    @Column(comment = "组织机构ID")
    @Schema(title = "组织机构ID")
    private Integer organizationId;

    /**
     * 用户类型
     */
    @Column(comment = "1:系统用户， 2：客户用户")
    @Schema(title = "1:系统用户， 2：客户用户")
    private Integer type;
    
}
