package com.schilings.neiko.notify.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户公告表
 */
@Data
@TableName("nk_notify_user_announcement")
@Schema(title = "用户公告表")
public class UserAnnouncement {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    @Column(comment = "ID")
    @Schema(title = "ID")
    private Long id;

    /**
     * 公告id
     */
    @Column(comment = "公告id")
    @Schema(title = "公告id")
    private Long announcementId;

    /**
     * 用户ID
     */
    @Column(comment = "用户ID")
    @Schema(title = "用户ID")
    private Long userId;

    /**
     * 状态，已读(1)|未读(0)
     */
    @Column(comment = "状态，已读(1)|未读(0)")
    @Schema(title = "状态，已读(1)|未读(0)")
    private Integer state;

    /**
     * 阅读时间
     */
    @Column(comment = "阅读时间")
    @Schema(title = "阅读时间")
    private LocalDateTime readTime;

    /**
     * 拉取时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Column(comment = "拉取时间")
    @Schema(title = "拉取时间")
    private LocalDateTime createTime;
}
