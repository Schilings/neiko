package com.schilings.neiko.wechat.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.schilings.neiko.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(title = "微信公众号文章")
@TableName("nk_wechat_user")
public class WechatUser extends LogicDeletedBaseEntity {

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Column(comment = "ID")
	private Long id;

	/**
	 * 应用类型(1:小程序，2:公众号)
	 */
	@Column(comment = "应用类型(1:小程序，2:公众号)")
	private Integer appType;

	/**
	 * 是否订阅（0：是；1：否；2：网页授权用户）
	 */
	@Column(comment = "是否订阅（0：是；1：否；2：网页授权用户）")
	private Integer subscribe;

	/**
	 * 返回用户关注的渠道来源， ADD_SCENE_SEARCH 公众号搜索， ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，
	 * ADD_SCENE_PROFILE_CARD 名片分享， ADD_SCENE_QR_CODE 扫描二维码， ADD_SCENEPROFILE LINK
	 * 图文页内名称点击， ADD_SCENE_PROFILE_ITEM 图文页右上角菜单， ADD_SCENE_PAID 支付后关注，ADD_SCENE_OTHERS 其他
	 */
	@Column(comment = "返回用户关注的渠道来源")
	private String subscribeScene;

	/**
	 * 关注次数
	 */
	@Column(comment = "关注次数")
	private Integer subscribeNum;

	/**
	 * 用户标识
	 */
	@Column(comment = "用户标识")
	private String openId;

	/**
	 * 昵称
	 */
	@Column(comment = "昵称")
	private String nickName;

	/**
	 * 性别（1：男，2：女，0：未知）
	 */
	@Column(comment = "性别（1：男，2：女，0：未知")
	private Integer sex;

	/**
	 * 手机号码
	 */
	@Column(comment = "手机号码")
	private String phone;

	/**
	 * 所在城市
	 */
	@Column(comment = "所在城市")
	private String city;

	/**
	 * 所在国家
	 */
	@Column(comment = "所在国家")
	private String country;

	/**
	 * 所在省份
	 */
	@Column(comment = "所在省份")
	private String province;

	/**
	 * 关注时间
	 */
	@Column(comment = "关注时间")
	private LocalDateTime subscribeTime;

	/**
	 * 取消关注时间
	 */
	@Column(comment = "取消关注时间")
	private LocalDateTime cancelSubscribeTime;

	/**
	 * 用户语言
	 */
	@Column(comment = "用户语言")
	private String language;

	/**
	 * 头像
	 */
	@Column(comment = "头像")
	private String headimgUrl;

	/**
	 * union_id
	 */
	@Column(comment = "union_id")
	private String unionId;

	/**
	 * 用户组
	 */
	@Column(comment = "用户组")
	private String groupId;

	/**
	 * 二维码扫码场景
	 */
	@Column(comment = "二维码扫码场景")
	private String qrSceneStr;

	/**
	 * 地理位置纬度
	 */
	@Column(comment = "地理位置纬度")
	private Double latitude;

	/**
	 * 地理位置经度
	 */
	@Column(comment = "地理位置经度")
	private Double longitude;

	/**
	 * 地理位置精度
	 */
	@TableField(value = "`precision`")
	@Column(comment = "地理位置精度")
	private Double precision;

	/**
	 * 会话密钥
	 */
	@Column(comment = "会话密钥")
	private String sessionKey;

}
