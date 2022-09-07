package com.schilings.neiko.wechat.model.dto;

import lombok.Data;

/**
 * 微信开发数据
 */
@Data
public class WechatOpenDataDTO {

	private String appId;

	private String userId;

	private String encryptedData;

	private String errMsg;

	private String iv;

	private String rawData;

	private String signature;

	private String sessionKey;

}
