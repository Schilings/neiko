package com.schilings.neiko.k8s.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@EnableScheduling
@EnableDiscoveryClient(autoRegister = false)
@SpringBootApplication
public class UpmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpmsApplication.class);
	}

	@Value("${dummy.message:no message}")
	private String value;

	@Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
	public void sout() {
		System.out.println(value);
	}

}
