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

	@Value("${sa-token.token-name}")
	private String tokenName;

	@Bean
	public OpenAPI openAPI() {
		OpenAPI openAPI = new OpenAPI();
		openAPI.components(new Components()
				.addSecuritySchemes("client",
						new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER)
								.name("access_token"))
				.addSecuritySchemes("user", new SecurityScheme().type(SecurityScheme.Type.APIKEY)
						.in(SecurityScheme.In.HEADER).name(tokenName)));
		return openAPI;
	}

}
