package com.schilings.neiko.samples.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@MapperScan({ "com.schilings.neiko.samples.admin" })
@SpringBootApplication
public class AdminApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(AdminApplication.class, args);

	}

}
