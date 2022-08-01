package com.schilings.neiko.autoconfigure.excel;

import com.schilings.neiko.common.excel.aop.RequestExcelArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@AutoConfiguration
@RequiredArgsConstructor
public class NeikoExcelAutoConfiguration {

	private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

	/**
	 * 追加 Excel 请求处理器 到 springmvc 中
	 */
	@PostConstruct
	public void setRequestExcelArgumentResolver() {
		List<HandlerMethodArgumentResolver> argumentResolvers = requestMappingHandlerAdapter.getArgumentResolvers();
		List<HandlerMethodArgumentResolver> resolverList = new ArrayList<>();
		resolverList.add(new RequestExcelArgumentResolver(requestMappingHandlerAdapter.getMessageConverters()));
		resolverList.addAll(argumentResolvers);
		requestMappingHandlerAdapter.setArgumentResolvers(resolverList);
	}

}
