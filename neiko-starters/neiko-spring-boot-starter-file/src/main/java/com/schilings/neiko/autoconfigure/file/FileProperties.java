package com.schilings.neiko.autoconfigure.file;


import com.schilings.neiko.autoconfigure.file.ftp.FtpMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = FileProperties.PREFIX)
public class FileProperties {

	public static final String PREFIX = "neiko.file";

	public static final String PREFIX_FTP = PREFIX + ".ftp";

	public static final String PREFIX_LOCAL = PREFIX + ".local";

	private LocalProperties local;

	private FtpProperties ftp;

	@Data
	public static class LocalProperties {

		/**
		 * <h1>本地文件存放原始路径</h1>
		 * <p>
		 * 如果为Null, 默认存放在系统临时目录下
		 * </p>
		 */
		private String path;

	}

	@Data
	public static class FtpProperties {

		/**
		 * 目标ip
		 */
		private String ip;

		/**
		 * ftp端口
		 */
		private Integer port = 21;

		/**
		 * 用户名
		 */
		private String username;

		/**
		 * 密码
		 */
		private String password;

		/**
		 * ftp路径
		 */
		private String path = "/";

		/**
		 * 模式
		 */
		private FtpMode mode;

		/**
		 * 字符集
		 */
		private String encoding = "UTF-8";

	}

}
