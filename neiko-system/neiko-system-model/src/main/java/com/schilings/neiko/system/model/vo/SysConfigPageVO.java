package com.schilings.neiko.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(title = "基础配置")
public class SysConfigPageVO {

    /**
     * 主键
     */
    @Schema(title = "主键ID")
    private Long id;

    /**
     * 配置名称
     */
    @Schema(title = "配置名称")
    private String name;

    /**
     * 配置在缓存中的key名
     */
    @Schema(title = "配置在缓存中的key名")
    private String confKey;

    /**
     * 配置值
     */
    @Schema(title = "配置值")
    private String confValue;

    /**
     * 分类
     */
    @Schema(title = "分类")
    private String category;

    /**
     * 描述
     */
    @Schema(title = "描述")
    private String remarks;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Schema(title = "修改时间")
    private LocalDateTime updateTime;
    
}
