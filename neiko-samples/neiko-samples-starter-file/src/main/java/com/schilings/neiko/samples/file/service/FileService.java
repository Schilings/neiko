package com.schilings.neiko.samples.file.service;


import com.schilings.neiko.autoconfigure.file.core.FileClient;
import com.schilings.neiko.autoconfigure.oss.OssClient;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FileService {

    private OssClient ossClient;

    private final FileClient fileClient;

    public FileService(ApplicationContext context) {
        try {
            ossClient = context.getBean(OssClient.class);
        }
        catch (Exception ignore) {
            ossClient = null;
        }

        // oss 为空或者未配置
        if (ossClient == null || !ossClient.isEnable()) {
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

        return ossClient.upload(stream, relativePath, size);
    }

}
