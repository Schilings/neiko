package com.schilings.neiko.autoconfigure.file.core;

/**
 * copy from 疯狂的狮子Li
 */
public abstract class AbstractFileClient implements FileClient {

	protected String slash = "/";

	protected String rootPath;

	/**
	 * 获取操作的根路径
	 */
	public String getRoot() {
		return rootPath;
	}

	/**
	 * 获取完整路径
	 * @param relativePath 文件相对 getRoot() 的路径
	 */
	public String getWholePath(String relativePath) {
		if (relativePath.startsWith(slash)) {
			return getRoot() + relativePath.substring(1);
		}
		return getRoot() + relativePath;
	}

}
