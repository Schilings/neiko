package com.schilings.neiko.samples.file.service;

import com.schilings.neiko.autoconfigure.file.core.FileClient;
import com.schilings.neiko.autoconfigure.oss.OssClient;
import com.schilings.neiko.autoconfigure.oss.OssTemplate;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FileService {

	private OssTemplate ossTemplate;

	private final FileClient fileClient;

	public FileService(ApplicationContext context) {
		try {
			ossTemplate = context.getBean(OssTemplate.class);
		}
		catch (Exception ignore) {
			ossTemplate = null;
		}

		// oss 为空或者未配置
		if (ossTemplate == null) {
			fileClient = context.getBean(FileClient.class);
		}
		else {
			fileClient = null;
		}
	}

	public String upload(InputStream stream, String relativePath, Long size) throws IOException {
		if (fileClient != null) {
			return fileClient.upload(stream, relativePath);
		}

		return null;
		//return ossTemplate.upload(stream, relativePath, size);
	}

	protected void createBucket(String bucket) {
		ossTemplate.createBucket(bucket);
	}

	protected void deleteBucket(String bucket) {
		ossTemplate.deleteBucket(bucket);
	}
	

}
