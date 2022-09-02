package com.schilings.neiko.autoconfigure.oss;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OssConstants {

	public static final String SLASH = "/";

	public static final String DOT = ".";

	/**
	 * 亚马逊 国际 后缀
	 */
	public static final String AWS_INTERNATIONAL = "amazonaws.com";

	/**
	 * 亚马逊 国内 后缀
	 */
	public static final String AWS_CN = "amazonaws.com.cn";

	public static final String S3 = "s3.";

}
