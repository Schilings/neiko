package com.schilings.neiko.samples.admin;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@MapperScan({ "com.schilings.neiko.samples.admin", "com.gitee.sunchenbin.mybatis.actable.dao.*" }) // 自动建表
@SpringBootApplication(
		scanBasePackages = { "com.schilings.neiko.samples.admin", "com.gitee.sunchenbin.mybatis.actable.manager.*" // 自动建表)
		})
public class AdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

}
