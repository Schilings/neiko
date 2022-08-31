package com.schilings.neiko.autoconfigure.file.local;

import cn.hutool.core.io.FileUtil;

import com.schilings.neiko.autoconfigure.file.FileProperties;
import com.schilings.neiko.autoconfigure.file.core.AbstractFileClient;
import com.schilings.neiko.autoconfigure.file.exception.FileException;
import com.schilings.neiko.common.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class LocalFileClient extends AbstractFileClient {

	private final File parentDir;

	public LocalFileClient(FileProperties.LocalProperties properties) throws IOException {
		final File dir = StringUtils.hasText(properties.getPath()) ? new File(properties.getPath())
				: FileUtil.getTmpDir();
		// 不存在且创建失败
		if (!dir.exists() && !dir.mkdirs()) {
			throw new FileException(String.format("路径: %s; 不存在且创建失败! 请检查是否拥有对该路径的操作权限!", dir.getPath()));
		}
		parentDir = dir;
		super.rootPath = dir.getPath();
		super.slash = File.separator;
	}

	/**
	 *
	 * 文件上传
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @param stream 文件输入流
	 * @return 文件绝对路径
	 * @author lingting 2021-10-19 22:32
	 * @author 疯狂的狮子Li 2022-04-24
	 */
	@Override
	public String upload(InputStream stream, String relativePath) throws IOException {
		// 目标文件
		final File file = new File(parentDir, relativePath);

		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			throw new FileException("文件上传失败! 创建父级文件夹失败! 父级路径: " + file.getParentFile().getPath());
		}

		if (!file.exists() && !file.createNewFile()) {
			throw new FileException("文件上传失败! 创建文件失败! 文件路径: " + file.getPath());
		}

		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			
			StreamUtils.write(stream, outputStream);
		}

		return relativePath;
	}

	/**
	 * 下载文件
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @return java.io.FileOutputStream 文件流
	 * @author lingting 2021-10-18 16:48
	 * @author 疯狂的狮子Li 2022-04-24
	 */
	@Override
	public File download(String relativePath) throws IOException {
		return new File(parentDir, relativePath);
	}

	/**
	 * 删除文件
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @return boolean
	 * @author lingting 2021-10-18 17:14
	 */
	@Override
	public boolean delete(String relativePath) throws IOException {
		final File file = new File(parentDir, relativePath);
		return file.exists() && file.delete();
	}

}
