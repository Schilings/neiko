package com.schilings.neiko.wechat.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <p>微信公众号菜单</p>
 * 
 * @author Schilings
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "微信公众号菜单")
public class WechatMpMenuButton implements Serializable {

    private String type;

    private String name;

    private String key;

    private String url;

    private String media_id;

    private String appid;

    private String pagepath;

    private List<WechatMpMenuButton> sub_button = new ArrayList();
    /**
     * content内容
     */
    private String content;

    private String repContent;
    /**
     * 消息类型
     */
    private String repType;
    /**
     * 消息名
     */
    private String repName;
    /**
     * 视频和音乐的描述
     */
    private String repDesc;
    /**
     * 视频和音乐的描述
     */
    private String repUrl;
    /**
     * 高质量链接
     */
    private String repHqUrl;
    /**
     * 缩略图的媒体id
     */
    private String repThumbMediaId;
    /**
     * 缩略图url
     */
    private String repThumbUrl;


}
