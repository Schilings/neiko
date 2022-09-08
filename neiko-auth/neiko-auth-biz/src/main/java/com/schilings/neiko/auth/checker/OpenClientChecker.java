package com.schilings.neiko.auth.checker;

/**
 *
 * <p>
 * 验证是否为开发客户端接口
 * </p>
 *
 * @author Schilings
 */
public interface OpenClientChecker {

	boolean isOpenByClientId(String clientId);
	
}
