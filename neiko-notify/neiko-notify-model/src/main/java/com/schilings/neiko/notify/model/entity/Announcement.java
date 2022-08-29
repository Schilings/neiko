package com.schilings.neiko.notify.model.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.schilings.neiko.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "nk_notify_announcement",autoResultMap = true)
@Schema(title = "公告信息")
public class Announcement extends LogicDeletedBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    @Column(comment = "ID")
    @Schema(title = "ID")
    private Long id;

    /**
     * 标题
     */
    @Column(comment = "标题")
    @Schema(title = "标题")
    private String title;

    /**
     * 内容
     */
    @Column(comment = "内容",type = MySqlTypeConstant.TEXT)
    @Schema(title = "内容")
    private String content;

    /**
     * 接收人筛选方式
     */
    @Column(comment = "接收人筛选方式")
    @Schema(title = "接收人筛选方式")
    private Integer recipientFilterType;

    /**
     * 对应接收人筛选方式的条件信息，多个用逗号分割。如角色标识，组织ID，用户类型，用户ID等
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Column(comment = "对应接收人筛选方式的条件信息。如角色标识，组织ID，用户类型，用户ID等",type = MySqlTypeConstant.JSON)
    @Schema(title = "对应接收人筛选方式的条件信息。如角色标识，组织ID，用户类型，用户ID等")
    private List<Object> recipientFilterCondition;

    /**
     * 接收方式，值与通知渠道一一对应
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Column(comment = "接收方式",type = MySqlTypeConstant.JSON)
    @Schema(title = "接收方式")
    private List<Integer> receiveMode;

    /**
     * 状态
     */
    @Column(comment = "状态")
    @Schema(title = "状态")
    private Integer status;

    /**
     * 永久有效的
     */
    @Column(comment = "永久有效的")
    @Schema(title = "永久有效的")
    private Integer immortal;

    /**
     * 截止日期
     */
    @Column(comment = "截止日期")
    @Schema(title = "截止日期")
    private LocalDateTime deadline;

}
