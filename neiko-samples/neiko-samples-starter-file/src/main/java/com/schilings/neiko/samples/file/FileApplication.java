package com.schilings.neiko.samples.file;

import com.schilings.neiko.samples.file.service.FileService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.io.FileInputStream;

@SpringBootApplication
public class FileApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(FileApplication.class, args);
		FileService fileService = applicationContext.getBean(FileService.class);

	}

}
