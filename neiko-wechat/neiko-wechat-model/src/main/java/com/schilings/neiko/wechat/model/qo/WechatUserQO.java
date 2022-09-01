package com.schilings.neiko.wechat.model.qo;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

/**
 *
 * <p>
 * 微信关联用户多条件查询PO
 * </p>
 *
 * @author Schilings
 */
@Data
@Schema(title = "微信关联用户查询对象")
@ParameterObject
public class WechatUserQO {
    

    /**
     * 应用类型(1:小程序，2:公众号)
     */
    @Parameter(description = "应用类型(1:小程序，2:公众号)")
    private Integer appType;

    /**
     * 是否订阅（0：否；1：是；2：网页授权用户）
     */
    @Parameter(description = "是否订阅（0：否；1：是；2：网页授权用户）")
    private Integer subscribe;

    /**
     * 返回用户关注的渠道来源，
     * ADD_SCENE_SEARCH 公众号搜索，
     * ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，
     * ADD_SCENE_PROFILE_CARD 名片分享，
     * ADD_SCENE_QR_CODE 扫描二维码，
     * ADD_SCENEPROFILE LINK 图文页内名称点击，
     * ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，
     * ADD_SCENE_PAID 支付后关注，
     * ADD_SCENE_OTHERS 其他
     */
    @Parameter(description = "用户关注的渠道来源，")
    private String subscribeScene;
    
    /**
     * 昵称
     */
    @Parameter(description = "昵称")
    private String nickName;

    /**
     * 性别（1：男，2：女，0：未知）
     */
    @Parameter(description = "性别（1：男，2：女，0：未知）")
    private Integer sex;

    /**
     * 手机号码
     */
    @Parameter(description = "手机号码")
    private String phone;
    
    

}
