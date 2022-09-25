package com.schilings.neiko.extend.sa.token.properties;

import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 *
 * <p>
 * 授权服务器的配置文件
 * </p>
 *
 * @author Schilings
 */
@Getter
@Setter
@ConfigurationProperties(prefix = OAuth2AuthorizationServerProperties.PREFIX)
public class OAuth2AuthorizationServerProperties {

	public static final String PREFIX = "neiko.sa-token.oauth2.authorization-server";

	/**
	 * 登陆验证码开关
	 */
	private boolean loginCaptchaEnabled = true;

	/**
	 * 是否开启对/oauth2/*路径请求的强制要求Post+Json
	 */
	private boolean enforceJsonFilter = true;

	/**
	 * Sa-Token-OAuth2 配置类 Model
	 *
	 * @author kong
	 */
	private SaOAuth2Config saOAuth2Config = new SaOAuth2Config();

	@Data
	public static class SaOAuth2Config implements Serializable {

		private static final long serialVersionUID = -6541180061782004705L;

		/**
		 * 是否打开模式：授权码（Authorization Code）
		 */
		public Boolean isCode = false;

		/**
		 * 是否打开模式：隐藏式（Implicit）
		 */
		public Boolean isImplicit = false;

		/**
		 * 是否打开模式：密码式（Password）
		 */
		public Boolean isPassword = true;

		/**
		 * 是否打开模式：凭证式（Client Credentials）
		 */
		public Boolean isClient = false;

		/**
		 * 是否在每次 Refresh-Token 刷新 Access-Token 时，产生一个新的 Refresh-Token
		 */
		public Boolean isNewRefresh = true;

		/**
		 * Code授权码 保存的时间(单位：秒) 默认五分钟
		 */
		public long codeTimeout = 60 * 5;

		/**
		 * Access-Token 保存的时间(单位：秒) 默认两个小时
		 */
		public long accessTokenTimeout = 60 * 60 * 2;

		/**
		 * Refresh-Token 保存的时间(单位：秒) 默认30 天
		 */
		public long refreshTokenTimeout = 60 * 60 * 24 * 10;

		/**
		 * Client-Token 保存的时间(单位：秒) 默认两个小时
		 */
		public long clientTokenTimeout = 60 * 60 * 2;

		/**
		 * Past-Client-Token 保存的时间(单位：秒) 默认为 -1，代表延续 Client-Token有效期
		 */
		public long pastClientTokenTimeout = -1;

	}

}
