package com.schilings.neiko.samples.cloud.openfeign;

import com.schilings.neiko.samples.cloud.openfeign.feign.ProviderRemoteService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@EnableFeignClients
@SpringBootApplication
public class NeikoOpenFeignApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(NeikoOpenFeignApplication.class,
				args);
		ProviderRemoteService bean = applicationContext.getBean(ProviderRemoteService.class);
	}

}
