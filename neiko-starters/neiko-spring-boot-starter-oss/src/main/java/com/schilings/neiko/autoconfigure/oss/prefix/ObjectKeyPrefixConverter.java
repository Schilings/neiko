package com.schilings.neiko.autoconfigure.oss.prefix;

/**
 * 存储对象 key前缀生成器
 *
 */
public interface ObjectKeyPrefixConverter {

	/**
	 * 生成前缀
	 * @return 前缀
	 */
	String getPrefix();

	/**
	 * 前置匹配，是否走添加前缀规则
	 * @return 是否匹配
	 */
	boolean match();

	/**
	 * 去除key前缀
	 * @param key key字节数组
	 * @return 原始key
	 */
	String unwrap(String key);

	/**
	 * 给key加上固定前缀
	 * @param key 原始key字节数组
	 * @return 加前缀之后的key
	 */
	String wrap(String key);

}
