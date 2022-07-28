package com.schilings.neiko.common.redis.core.prefix.impl;


import com.schilings.neiko.common.redis.core.prefix.IRedisPrefixConverter;

/**
 * redis key前缀默认转换器
 *
 * @author huyuanzhi
 */
public class DefaultRedisPrefixConverter implements IRedisPrefixConverter {

	private final String prefix;

	public DefaultRedisPrefixConverter(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public String getPrefix() {
		return prefix;
	}

	@Override
	public boolean match() {
		return true;
	}

}
