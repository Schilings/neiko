package com.schilings.neiko.extend.sa.token.properties;

import cn.dev33.satoken.config.SaCookieConfig;
import cn.dev33.satoken.config.SaTokenConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 *
 * <p>
 * </p>
 *
 * @author Schilings
 */
@Data
@ConfigurationProperties(prefix = ExtendSaTokenProperties.PREFIX)
public class ExtendSaTokenProperties {

	public static final String PREFIX = "neiko.sa-token";

	/**
	 * Sa-Token 配置类 Model
	 * <p>
	 * 你可以通过yml、properties、java代码等形式配置本类参数，具体请查阅官方文档: http://sa-token.dev33.cn/
	 *
	 * @author kong
	 */
	private SaTokenConfig saTokenConfig = new SaTokenConfig();

	@Data
	public static class SaTokenConfig implements Serializable {

		private static final long serialVersionUID = -6541180061782004705L;

		// =====================双方============================

		/** token前缀, 格式样例(satoken: Bearer xxxx-xxxx-xxxx-xxxx) */
		private String tokenPrefix;

		/** 是否在初始化配置时打印版本字符画 */
		private Boolean isPrint = false;

		/** 是否打印操作日志 */
		private Boolean isLog = false;

		// ======================资源Server======================

		/** token名称 (同时也是cookie名称) */
		private String tokenName = "access_token";

		/** 是否尝试从请求体里读取token */
		private Boolean isReadBody = false;

		/** 是否尝试从header里读取token */
		private Boolean isReadHead = true;

		/** 是否尝试从cookie里读取token */
		private Boolean isReadCookie = false;

		/**
		 * Http Basic 认证的账号和密码
		 */
		private String basic = "";

		/** 配置当前项目的网络访问地址 */
		private String currDomain;

		// =====================授权Server=====================

		/** token的长久有效期(单位:秒) 默认30天, -1代表永久 */
		private long timeout = 60 * 60 * 2;

		/**
		 * token临时有效期 [指定时间内无操作就视为token过期] (单位: 秒), 默认-1 代表不限制 (例如可以设置为1800代表30分钟内无操作就过期)
		 */
		private long activityTimeout = 60 * 60;

		/** 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录) */
		private Boolean isConcurrent = true;

		/** 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token) */
		private Boolean isShare = true;

		/** 是否打开自动续签 (如果此值为true, 框架会在每次直接或间接调用getLoginId()时进行一次过期检查与续签操作) */
		private Boolean autoRenew = true;

		/**
		 * 同一账号最大登录数量，-1代表不限 （只有在 isConcurrent=true, isShare=false 时此配置才有效）
		 */
		private int maxLoginCount = 12;

		/** token风格(默认可取值：uuid、simple-uuid、random-32、random-64、random-128、tik) */
		private String tokenStyle = "random-64";

		/** 默认dao层实现类中，每次清理过期数据间隔的时间 (单位: 秒) ，默认值30秒，设置为-1代表不启动定时清理 */
		private int dataRefreshPeriod = 30;

		/** 获取[token专属session]时是否必须登录 (如果配置为true，会在每次获取[token-session]时校验是否登录) */
		private Boolean tokenSessionCheckLogin = true;

	}

}
