package com.schilings.neiko.samples.admin;

import com.schilings.neiko.autoconfigure.log.annotation.EnableAccessLog;
import com.schilings.neiko.autoconfigure.log.annotation.EnableOperationLog;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableOperationLog
@EnableAccessLog
@SpringBootApplication
public class AdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}
	

}
