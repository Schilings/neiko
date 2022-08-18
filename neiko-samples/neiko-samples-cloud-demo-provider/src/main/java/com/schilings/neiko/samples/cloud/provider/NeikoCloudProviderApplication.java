package com.schilings.neiko.samples.cloud.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NeikoCloudProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeikoCloudProviderApplication.class, args);
	}

}
