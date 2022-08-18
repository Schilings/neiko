package com.schilings.neiko.samples.starters.web;

import com.schilings.neiko.autoconfigure.log.annotation.EnableAccessLog;
import com.schilings.neiko.autoconfigure.log.annotation.EnableOperationLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

//@EnableOperationLog
//@EnableAccessLog
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
