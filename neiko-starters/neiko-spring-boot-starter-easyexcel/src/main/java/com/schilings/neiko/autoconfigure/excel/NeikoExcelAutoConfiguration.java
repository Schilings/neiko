package com.schilings.neiko.autoconfigure.excel;

import com.schilings.neiko.common.excel.aop.RequestExcelArgumentResolver;
import com.schilings.neiko.common.excel.aop.ResponseExcelReturnValueHandler;
import com.schilings.neiko.common.excel.handler.response.MultiSheetWriteHandler;
import com.schilings.neiko.common.excel.handler.response.SheetWriteHandler;
import com.schilings.neiko.common.excel.handler.response.SingleSheetWriteHandler;
import com.schilings.neiko.common.excel.properties.ExcelConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@AutoConfiguration
@RequiredArgsConstructor
@Import(ExcelHandlerConfiguration.class)
@EnableConfigurationProperties(ExcelConfigProperties.class)
public class NeikoExcelAutoConfiguration {

	private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

	private final List<SheetWriteHandler> sheetWriteHandlerList;

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

	/**
	 * 追加 Excel文件的 response 处理器 到 springmvc 中
	 */
	@PostConstruct
	public void setReturnValueHandlers() {

		List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter
				.getReturnValueHandlers();
		List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();
		handlers.add(new ResponseExcelReturnValueHandler(sheetWriteHandlerList));
		handlers.addAll(returnValueHandlers);
		requestMappingHandlerAdapter.setReturnValueHandlers(handlers);
	}

}
