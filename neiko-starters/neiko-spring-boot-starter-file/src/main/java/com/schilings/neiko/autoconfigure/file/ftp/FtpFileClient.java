package com.schilings.neiko.autoconfigure.file.ftp;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpConfig;

import com.schilings.neiko.autoconfigure.file.FileProperties;
import com.schilings.neiko.autoconfigure.file.core.AbstractFileClient;
import com.schilings.neiko.autoconfigure.file.exception.FileException;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;


public class FtpFileClient extends AbstractFileClient {

	private final Ftp client;

	public FtpFileClient(FileProperties.FtpProperties properties) {
		FtpConfig config = new FtpConfig().setHost(properties.getIp()).setPort(properties.getPort())
				.setUser(properties.getUsername()).setPassword(properties.getPassword())
				.setCharset(Charset.forName(properties.getEncoding()));

		final FtpMode mode = properties.getMode();
		if (mode == FtpMode.ACTIVE) {
			client = new Ftp(config, cn.hutool.extra.ftp.FtpMode.Active);
		}
		else if (mode == FtpMode.PASSIVE) {
			client = new Ftp(config, cn.hutool.extra.ftp.FtpMode.Passive);
		}
		else {
			client = new Ftp(config, null);
		}

		if (!StringUtils.hasText(properties.getPath())) {
			throw new NullPointerException("ftp文件根路径不能为空!");
		}

		super.rootPath = properties.getPath().endsWith(super.slash) ? properties.getPath()
				: properties.getPath() + super.slash;
	}

	/**
	 * 上传文件 - 不会关闭流. 请在成功后手动关闭
	 * @param stream 文件流
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @return java.lang.String 文件完整路径
	 * @author lingting 2021-10-18 11:40
	 * @author 疯狂的狮子Li 2022-04-24
	 */
	@Override
	public String upload(InputStream stream, String relativePath) throws IOException {
		final String path = getWholePath(relativePath);
		final String fileName = FileUtil.getName(path);
		final String dir = StrUtil.removeSuffix(path, fileName);
		// 上传失败
		if (!client.upload(dir, fileName, stream)) {
			throw new FileException(
					String.format("文件上传失败! 相对路径: %s; 根路径: %s; 请检查此路径是否存在以及登录用户是否拥有操作权限!", relativePath, path));
		}
		return path;
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
		final String path = getWholePath(relativePath);
		final String fileName = FileUtil.getName(path);
		final String dir = StrUtil.removeSuffix(path, fileName);
		// 临时文件
		File tmpFile = FileUtil.createTempFile();
		tmpFile = FileUtil.rename(tmpFile, fileName, true);
		// 输出流
		try (FileOutputStream outputStream = new FileOutputStream(tmpFile)) {
			client.download(dir, fileName, outputStream);
		}
		return tmpFile;
	}

	/**
	 * 删除文件
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @return boolean
	 * @author lingting 2021-10-18 17:14
	 * @author 疯狂的狮子Li 2022-04-24
	 */
	@Override
	public boolean delete(String relativePath) throws IOException {
		return client.delFile(getWholePath(relativePath));
	}

}
