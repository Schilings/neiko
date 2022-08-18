package com.schilings.neiko.cloud.context.bootstrap.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.bootstrap.BootstrapApplicationListener;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 对于类型为 ApplicationContextInitializer 的@Beans 有一个特殊的契约。如果要控制启动顺序，可以使用@Order
 * 注释(默认顺序是最后一个)标记类。
 *
 * //如果自动装配的BootstrapConfiguration类是ApplicationContextInitializer类型的
 * //那么父上下文会把它容器中的所有初始化添加到主上下文
 *
 * @see BootstrapApplicationListener#apply(ConfigurableApplicationContext,
 * org.springframework.boot.SpringApplication,
 * org.springframework.core.env.ConfigurableEnvironment)
 */

@Slf4j
public class CustomBootstrapConfiguration implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Bean
	PropertySourceLocator customPropertySourceLocator() {
		return new CustomPropertySourceLocator();
	}

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		System.out.println("initialize.................");
	}

}
