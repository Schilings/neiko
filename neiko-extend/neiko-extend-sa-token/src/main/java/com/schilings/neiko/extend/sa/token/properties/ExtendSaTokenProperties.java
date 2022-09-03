package com.schilings.neiko.extend.sa.token.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * <p>
 * </p>
 *
 * @author Schilings
 */
@Data
@ConfigurationProperties(prefix = "neiko.sa-token")
public class ExtendSaTokenProperties {

	/**
	 * 是否开启对/oauth2/*路径请求的强制要求Post+Json
	 */
	private boolean enforceJsonFilter = true;

	/**
	 * 是否强制关闭注解鉴权
	 */
	private boolean enforceCancelAuthenticate = false;

	private boolean checkLogin = true;
	private boolean checkRole = true;
	private boolean checkPermission = true;
	private boolean checkSafe = true;
	private boolean checkBasic = true;
	private boolean checkScope = true;

}
