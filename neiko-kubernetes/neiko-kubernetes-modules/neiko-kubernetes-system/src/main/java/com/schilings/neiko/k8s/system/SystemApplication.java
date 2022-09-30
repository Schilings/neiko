package com.schilings.neiko.k8s.system;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableDiscoveryClient(autoRegister = false)
@SpringBootApplication
public class SystemApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class);
    }
    
}
