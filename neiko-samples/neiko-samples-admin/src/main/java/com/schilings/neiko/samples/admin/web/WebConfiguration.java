package com.schilings.neiko.samples.admin.web;

import com.schilings.neiko.autoconfigure.file.FileProperties;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

	private final FileProperties fileProperties;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (StringUtils.hasText(fileProperties.getLocal().getPath())) {
			registry.addResourceHandler("/public/**")
					.addResourceLocations(ResourceUtils.FILE_URL_PREFIX + fileProperties.getLocal().getPath());
		}
	}

}
