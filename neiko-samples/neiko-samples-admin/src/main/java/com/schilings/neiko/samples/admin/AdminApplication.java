package com.schilings.neiko.samples.admin;

import com.schilings.neiko.autoconfigure.log.annotation.EnableAccessLog;
import com.schilings.neiko.autoconfigure.log.annotation.EnableOperationLog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableOperationLog
@EnableAccessLog
@MapperScan({"com.schilings.neiko.samples.admin","com.gitee.sunchenbin.mybatis.actable.dao.*"})// 自动建表
@SpringBootApplication(scanBasePackages = {
		"com.schilings.neiko.samples.admin",
		"com.gitee.sunchenbin.mybatis.actable.manager.*" // 自动建表)
})
public class AdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

}
