package com.schilings.neiko.wechat.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class WechatConst {

	@Getter
	@AllArgsConstructor
	public enum AppType {

		// 应用类型(1:小程序，2:公众号)
		MA(1), MP(2);

		private final Integer value;

	}

	@Getter
	@AllArgsConstructor
	public enum Subscribe {

		// 是否订阅（0：否；1：是；2：网页授权用户）

		NO(0), YES(1), WEB(2);

		private final Integer value;

	}

	@Getter
	@AllArgsConstructor
	public enum SubscribeScene {

		/**
		 * 返回用户关注的渠道来源， ADD_SCENE_SEARCH 公众号搜索， ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，
		 * ADD_SCENE_PROFILE_CARD 名片分享， ADD_SCENE_QR_CODE 扫描二维码， ADD_SCENEPROFILE LINK
		 * 图文页内名称点击， ADD_SCENE_PROFILE_ITEM 图文页右上角菜单， ADD_SCENE_PAID 支付后关注，
		 * ADD_SCENE_OTHERS 其他
		 */
		ADD_SCENE_SEARCH("ADD_SCENE_SEARCH"), ADD_SCENE_ACCOUNT_MIGRATION(
				"ADD_SCENE_ACCOUNT_MIGRATION"), ADD_SCENE_PROFILE_CARD("ADD_SCENE_PROFILE_CARD"), ADD_SCENE_QR_CODE(
						"ADD_SCENE_QR_CODE"), ADD_SCENEPROFILE_LINK("ADD_SCENEPROFILE_LINK"), ADD_SCENE_PROFILE_ITEM(
								"ADD_SCENE_PROFILE_ITEM"), ADD_SCENE_PAID(
										"ADD_SCENE_PAID"), ADD_SCENE_OTHERS("ADD_SCENE_OTHERS");

		private final String value;

	}

	@Getter
	@AllArgsConstructor
	public enum Sex {

		// 性别（1：男，2：女，0：未知）
		MALE(1), FEMALE(2), UNKNOWN(0);

		private final Integer value;

	}

}
