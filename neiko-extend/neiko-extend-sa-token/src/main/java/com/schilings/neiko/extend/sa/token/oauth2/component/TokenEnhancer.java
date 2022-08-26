package com.schilings.neiko.extend.sa.token.oauth2.component;

import java.util.Map;

/**
 *
 * <p>
 * Token增强器
 * </p>
 *
 * @author Schilings
 */
public interface TokenEnhancer {

	Map<String, Object> enhance(Map<String, Object> tokenMap);

}
