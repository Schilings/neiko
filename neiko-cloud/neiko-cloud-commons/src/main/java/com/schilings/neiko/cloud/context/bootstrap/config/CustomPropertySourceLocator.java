package com.schilings.neiko.cloud.context.bootstrap.config;

import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Collections;

/**
 *
 * <p>
 * 添加的外部配置的默认属性源是 Spring Cloud Config Server，但是您可以通过将 PropertySourceLocator 类型的 bean *
 * 添加到引导上下文(通过 Spring.factories)来添加其他源。 * 例如，可以从其他服务器或数据库插入附加属性
 * </p>
 *
 * @author Schilings
 */
public class CustomPropertySourceLocator implements PropertySourceLocator {

	/**
	 * 传入的环境是即将创建的 ApplicationContext 的环境，换句话说，是我们提供其他属性源的环境。它已经有了普通 Spring boot 提供的属性源，
	 * 因此您可以使用这些属性源来定位特定于此环境的属性源(例如，通过键入 Spring.application.name 属性，就像在缺省的 Spring Cloud
	 * Config Server 属性源定位器中所做的那样)。
	 * @param environment
	 * @return
	 */
	@Override
	public PropertySource<?> locate(Environment environment) {
		// 可以从数据库等地方添加属性
		return new MapPropertySource("customProperty",
				Collections.<String, Object>singletonMap("property.from.sample.custom.source", "worked as intended"));
	}

}
