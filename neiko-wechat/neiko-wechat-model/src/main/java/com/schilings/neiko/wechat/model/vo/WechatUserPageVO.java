package com.schilings.neiko.wechat.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(title = "微信关联用户分页VO")
public class WechatUserPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Schema(title = "创建时间")
    private Long id;

    /**
     * 应用类型(1:小程序，2:公众号)
     */
    @Schema(title = "应用类型")
    private Integer appType;

    /**
     * 是否订阅（0：是；1：否；2：网页授权用户）
     */
    @Schema(title = "是否订阅")
    private Integer subscribe;

    /**
     * 返回用户关注的渠道来源，
     * ADD_SCENE_SEARCH 公众号搜索，
     * ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，
     * ADD_SCENE_PROFILE_CARD 名片分享，
     * ADD_SCENE_QR_CODE 扫描二维码，
     * ADD_SCENEPROFILE LINK 图文页内名称点击，
     * ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，
     * ADD_SCENE_PAID 支付后关注，ADD_SCENE_OTHERS 其他
     */
    @Schema(title = "返回用户关注的渠道来源")
    private String subscribeScene;

    /**
     * 关注次数
     */
    @Schema(title = "关注次数")
    private Integer subscribeNum;

    /**
     * 用户标识
     */
    @Schema(title = "用户标识")
    private String openId;

    /**
     * 昵称
     */
    @Schema(title = "昵称")
    private String nickName;

    /**
     * 性别（1：男，2：女，0：未知）
     */
    @Schema(title = "性别")
    private Integer sex;

    /**
     * 手机号码
     */
    @Schema(title = "手机号码")
    private String phone;

    /**
     * 所在城市
     */
    @Schema(title = "所在城市")
    private String city;
    /**
     * 所在国家
     */
    @Schema(title = "所在国家")
    private String country;

    /**
     * 所在省份
     */
    @Schema(title = "所在省份")
    private String province;


    @Schema(title = "关注时间")
    private LocalDateTime subscribeTime;

    /**
     * 取消关注时间
     */
    @Schema(title = "取消关注时间")
    private LocalDateTime cancelSubscribeTime;

    /**
     * 用户语言
     */
    @Schema(title = "用户语言")
    private String language;
    /**
     * 头像
     */
    @Schema(title = "头像")
    private String headimgUrl;

    /**
     * union_id
     */
    @Schema(title = "union_id")
    private String unionId;

    /**
     * 用户组
     */
    @Schema(title = "用户组")
    private String groupId;
    
    
    /**
     * 地理位置纬度
     */
    @Schema(title = "地理位置纬度")
    private Double latitude;
    /**
     * 地理位置经度
     */
    @Schema(title = "地理位置经度")
    private Double longitude;
    


    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(title = "更新时间")
    private LocalDateTime updateTime;
}
