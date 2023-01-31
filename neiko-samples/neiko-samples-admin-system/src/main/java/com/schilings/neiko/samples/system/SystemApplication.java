package com.schilings.neiko.samples.system;


import com.schilings.neiko.security.oauth2.resource.server.autoconfigure.EnableResourceServer;
import com.schilings.neiko.security.properties.SecurityProperties;
import com.schilings.neiko.system.properties.SystemProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@MapperScan({ "com.schilings.neiko.**.mapper" })
@ComponentScan({ "com.schilings.neiko.samples.system",  "com.schilings.neiko.system","com.schilings.neiko.file" })
@EnableConfigurationProperties({SystemProperties.class, SecurityProperties.class})
@EnableResourceServer
@SpringBootApplication
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
}
