package com.schilings.neiko.autoconfigure.file;

import com.schilings.neiko.autoconfigure.file.core.FileClient;
import com.schilings.neiko.autoconfigure.file.ftp.FtpFileClient;
import com.schilings.neiko.autoconfigure.file.local.LocalFileClient;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;


@AutoConfiguration
@AllArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class FileAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(FileClient.class)
	@ConditionalOnProperty(prefix = FileProperties.PREFIX_FTP, name = "ip")
	public FileClient neikoFileFtpClient(FileProperties properties) {
		return new FtpFileClient(properties.getFtp());
	}

	@Bean
	@ConditionalOnMissingBean(FileClient.class)
	public FileClient neikoFileLocalClient(FileProperties properties) throws IOException {
		FileProperties.LocalProperties localProperties = properties == null || properties.getLocal() == null ? new FileProperties.LocalProperties()
				: properties.getLocal();
		return new LocalFileClient(localProperties);
	}

}
