package com.schilings.neiko.wechat.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;


import java.io.Serializable;

import java.util.Map;

/**
 * 
 * <p>微信公众号自定义回复</p>
 * 
 * @author Schilings
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "微信公众号自定义回复")
@TableName("nk_wechat_mp_reply")
public class WechatMpReply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Column(comment = "主键")
    @Schema(title = "主键")
    private Long id;
    
    /**
     * 类型（1、关注时回复；2、消息回复；3、关键词回复）
     */
    @Column(comment = "类型（1、关注时回复；2、消息回复；3、关键词回复）")
    @Schema(title = "类型（1、关注时回复；2、消息回复；3、关键词回复）")
    private Integer type;
    /**
     * 关键词
     */
    @Column(comment = "关键词")
    @Schema(title = "关键词")
    private String reqKey;
    
    /**
     * 请求消息类型（text：文本；image：图片；voice：语音；video：视频；shortvideo：小视频；location：地理位置）
     */
    @Column(comment = "请求消息类型（text：文本；image：图片；voice：语音；video：视频；shortvideo：小视频；location：地理位置）")
    @Schema(title = "请求消息类型（text：文本；image：图片；voice：语音；video：视频；shortvideo：小视频；location：地理位置）")
    private String reqType;
    
    /**
     * 回复消息类型（text：文本；image：图片；voice：语音；video：视频；music：音乐；news：图文）
     */
    @Column(comment = "回复消息类型（text：文本；image：图片；voice：语音；video：视频；music：音乐；news：图文）")
    @Schema(title = "回复消息类型（text：文本；image：图片；voice：语音；video：视频；music：音乐；news：图文）")
    private String repType;
    /**
     * 回复类型文本匹配类型（1、全匹配，2、半匹配）
     */
    @Column(comment = "回复类型文本匹配类型（1、全匹配，2、半匹配）")
    @Schema(title = "回复类型文本匹配类型（1、全匹配，2、半匹配）")
    private Integer repMate;
    /**
     * 回复类型文本保存文字
     */
    @Column(comment = "回复类型文本保存文字")
    @Schema(title = "回复类型文本保存文字\"")
    private String repContent;
    /**
     * 回复的素材名、视频和音乐的标题
     */
    @Column(comment = "回复的素材名、视频和音乐的标题")
    @Schema(title = "回复的素材名、视频和音乐的标题")
    private String repName;
    /**
     * 回复类型imge、voice、news、video的mediaID或音乐缩略图的媒体id
     */
    @Column(comment = "回复类型imge、voice、news、video的mediaID或音乐缩略图的媒体id")
    @Schema(title = "回复类型imge、voice、news、video的mediaID或音乐缩略图的媒体id")
    private String repMediaId;
    /**
     * 视频和音乐的描述
     */
    @Column(comment = "视频和音乐的描述")
    @Schema(title = "视频和音乐的描述")
    private String repDesc;
    /**
     * 链接
     */
    @Column(comment = "链接")
    @Schema(title = "链接")
    private String repUrl;
    /**
     * 高质量链接
     */
    @Column(comment = "高质量链接")
    @Schema(title = "高质量链接")
    private String repHqUrl;
    /**
     * 缩略图的媒体id
     */
    @Column(comment = "缩略图的媒体id")
    @Schema(title = "缩略图的媒体id")
    private String repThumbMediaId;
    /**
     * 缩略图url
     */
    @Column(comment = "缩略图url")
    @Schema(title = "缩略图url")
    private String repThumbUrl;

    /**
     * 图文消息的内容
     */
    @TableField(jdbcType = JdbcType.VARCHAR, typeHandler = JacksonTypeHandler.class)
    @Column(comment = "图文消息的内容",type = MySqlTypeConstant.JSON)
    @Schema(title = "图文消息的内容")
    private Map<String, Object> content;

}
