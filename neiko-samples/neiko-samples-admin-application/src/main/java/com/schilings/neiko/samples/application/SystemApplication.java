package com.schilings.neiko.samples.application;

import com.schilings.neiko.security.oauth2.resource.server.autoconfigure.EnableResourceServer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan({ "com.schilings.neiko.**.mapper" })
@EnableResourceServer
@SpringBootApplication
public class SystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SystemApplication.class, args);
	}

}
