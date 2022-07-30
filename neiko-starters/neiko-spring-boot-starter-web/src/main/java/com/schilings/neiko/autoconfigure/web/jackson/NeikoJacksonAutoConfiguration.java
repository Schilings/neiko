package com.schilings.neiko.autoconfigure.web.jackson;

import com.schilings.neiko.common.core.desensitize.JsonDesensitizeModule;
import com.schilings.neiko.common.core.desensitize.serializer.JsonDesensitizeSerializerModifier;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(before = { JacksonAutoConfiguration.class })
public class NeikoJacksonAutoConfiguration {

	/**
	 * 注册 Jackson 的脱敏模块
	 * @return Jackson2ObjectMapperBuilderCustomizer
	 */
	@Bean
	@ConditionalOnMissingBean(JsonDesensitizeModule.class)
	public JsonDesensitizeModule jsonDesensitizeModule(BeanFactory beanFactory) {
		JsonDesensitizeSerializerModifier desensitizeModifier = new JsonDesensitizeSerializerModifier(beanFactory);
		return new JsonDesensitizeModule(desensitizeModifier);
	}

}
