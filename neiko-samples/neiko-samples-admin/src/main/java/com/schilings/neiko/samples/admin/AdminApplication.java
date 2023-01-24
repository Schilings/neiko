package com.schilings.neiko.samples.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@MapperScan({ "com.schilings.neiko.samples.admin" })
@SpringBootApplication(scanBasePackages = { "com.schilings.neiko.samples.admin" })
public class AdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

}
