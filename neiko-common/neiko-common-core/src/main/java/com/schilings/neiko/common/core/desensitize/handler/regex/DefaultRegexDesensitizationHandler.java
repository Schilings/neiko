package com.schilings.neiko.common.core.desensitize.handler.regex;

/**
 * <pre>
 * <p>正则替换脱敏处理器，使用正则匹配替换处理原数据</p>
 * </pre>
 *
 * @author Schilings
 */
public class DefaultRegexDesensitizationHandler implements RegexDesensitizationHandler {

	@Override
	public String handle(String origin, String regex, String replacement) {
		return origin.replaceAll(regex, replacement);
	}

}
