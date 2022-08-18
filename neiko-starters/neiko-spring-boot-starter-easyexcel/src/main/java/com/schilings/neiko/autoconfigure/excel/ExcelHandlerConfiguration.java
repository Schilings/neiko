package com.schilings.neiko.autoconfigure.excel;

import com.alibaba.excel.converters.Converter;
import com.schilings.neiko.common.excel.handler.response.MultiSheetWriteHandler;
import com.schilings.neiko.common.excel.handler.response.SingleSheetWriteHandler;
import com.schilings.neiko.common.excel.handler.response.enhancer.WriterBuilderEnhancer;
import com.schilings.neiko.common.excel.properties.ExcelConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class ExcelHandlerConfiguration {

	private final ExcelConfigProperties configProperties;

	private final ObjectProvider<List<Converter<?>>> converterProvider;

	private final ObjectProvider<List<WriterBuilderEnhancer>> enhancesProvider;

	/**
	 * 单sheet 写入处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public SingleSheetWriteHandler singleSheetWriteHandler() {
		return new SingleSheetWriteHandler(converterProvider, enhancesProvider, configProperties);
	}

	/**
	 * 多sheet 写入处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public MultiSheetWriteHandler manySheetWriteHandler() {
		return new MultiSheetWriteHandler(converterProvider, enhancesProvider, configProperties);
	}

}
