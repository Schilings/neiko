package com.schilings.neiko.common.excel.handler.response;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.schilings.neiko.common.excel.annotation.ResponseExcel;
import com.schilings.neiko.common.excel.annotation.Sheet;
import com.schilings.neiko.common.excel.handler.response.converters.LocalDateStringConverter;
import com.schilings.neiko.common.excel.handler.response.converters.LocalDateTimeStringConverter;
import com.schilings.neiko.common.excel.handler.response.enhancer.WriterBuilderEnhancer;
import com.schilings.neiko.common.excel.handler.response.exception.ExcelException;
import com.schilings.neiko.common.excel.handler.response.head.HeadGenerator;
import com.schilings.neiko.common.excel.handler.response.head.HeadMeta;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractSheetWriteHandler implements SheetWriteHandler, ApplicationContextAware {

	private final ObjectProvider<List<Converter<?>>> converterProvider;

	private final ObjectProvider<List<WriterBuilderEnhancer>> enhancerProvider;

	private ApplicationContext applicationContext;

	@Override
	public void check(ResponseExcel responseExcel) {
		if (responseExcel.sheets().length == 0) {
			throw new ExcelException("@ResponseExcel sheet 配置不合法");
		}
	}

	@SneakyThrows(UnsupportedEncodingException.class)
	@Override
	public void export(Object o, HttpServletResponse response, ResponseExcel responseExcel) {
		// check
		check(responseExcel);

		// 解析文件名
		String name = responseExcel.name();
		if (name == null) {
			name = UUID.randomUUID().toString();
		}
		String fileName = String.format("%s%s", URLEncoder.encode(name, "UTF-8"), responseExcel.suffix().getValue());

		// 根据实际的文件类型找到对应的 contentType
		String contentType = MediaTypeFactory.getMediaType(fileName).map(MediaType::toString)
				.orElse("application/vnd.ms-excel");
		response.setContentType(contentType);
		response.setCharacterEncoding("utf-8");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);

		// write
		write(o, response, responseExcel);
	}

	/**
	 * 获取ExcelWriter
	 * @param response
	 * @param responseExcel
	 * @return
	 */
	@SneakyThrows(IOException.class)
	public ExcelWriter getExcelWriter(HttpServletResponse response, ResponseExcel responseExcel) {
		ExcelWriterBuilder writerBuilder =
				// 输出流
				EasyExcel.write(response.getOutputStream())
						// LocalDate转换器
						.registerConverter(LocalDateStringConverter.INSTANCE)
						// LocalDateTime转换器
						.registerConverter(LocalDateTimeStringConverter.INSTANCE)
						// 自动关闭输出流
						.autoCloseStream(true)
						// excel文件类型
						.excelType(responseExcel.suffix())
						// 内存中操作
						.inMemory(responseExcel.inMemory());

		// 文件加密
		if (StringUtils.hasText(responseExcel.password())) {
			writerBuilder.password(responseExcel.password());
		}
		// 包含字段
		if (responseExcel.include().length > 0) {
			writerBuilder.includeColumnFieldNames(Arrays.asList(responseExcel.include()));
		}
		// 排除字段
		if (responseExcel.exclude().length > 0) {
			writerBuilder.excludeColumnFieldNames(Arrays.asList(responseExcel.exclude()));
		}
		// 拦截器，自定义样式等处理器
		if (responseExcel.writeHandler().length > 0) {
			for (Class<? extends WriteHandler> clazz : responseExcel.writeHandler()) {
				writerBuilder.registerWriteHandler(BeanUtils.instantiateClass(clazz));
			}
		}
		// 自定义类型->String转换器
		if (responseExcel.converter().length > 0) {
			for (Class<? extends Converter> clazz : responseExcel.converter()) {
				writerBuilder.registerConverter(BeanUtils.instantiateClass(clazz));
			}
		}
		// 模板路径
		String templatePath = "";
		if (StringUtils.hasText(responseExcel.template())) {
			ClassPathResource classPathResource = new ClassPathResource(
					templatePath + File.separator + responseExcel.template());
			InputStream inputStream = classPathResource.getInputStream();
			writerBuilder.withTemplate(inputStream);
		}

		// 用于spring bean自定义注入的转换器
		registerCustomConverter(writerBuilder);
		// 用于spring bean自定义注入的增强器
		enhanceExcelWriterBuilder(writerBuilder, response, responseExcel, templatePath);

		// build
		return writerBuilder.build();
	}

	/**
	 * 获取WriteSheet
	 * @param sheet
	 * @param targetClass
	 * @param template
	 * @param bookHeadGeneratorClass
	 * @return
	 */
	public WriteSheet sheet(Sheet sheet, Class<?> targetClass, String template,
			Class<? extends HeadGenerator> bookHeadGeneratorClass) {
		// Sheet 编号和名称
		Integer sheetNo = sheet.sheetNo() >= 0 ? sheet.sheetNo() : null;
		String sheetName = sheet.sheetName();
		// 是否模板写入
		ExcelWriterSheetBuilder writerSheetBuilder = StringUtils.hasText(template) ? EasyExcel.writerSheet(sheetNo)
				: EasyExcel.writerSheet(sheetNo, sheetName);

		// 头信息增强优先使用 sheet 指定的头信息增强
		Class<? extends HeadGenerator> headGeneratorClass = null;
		if (isNotInterface(sheet.headGeneratorClass())) {
			headGeneratorClass = sheet.headGeneratorClass();
		}
		// 其次使用 @ResponseExcel 中定义的全局头信息增强
		else if (isNotInterface(bookHeadGeneratorClass)) {
			headGeneratorClass = bookHeadGeneratorClass;
		}

		// 自定义头信息增强则使用其生成头信息
		if (headGeneratorClass != null) {
			fillCustomHeadInfo(targetClass, bookHeadGeneratorClass, writerSheetBuilder);
		}
		// 否则使用 dataClass 来自动获取
		else if (targetClass != null) {
			writerSheetBuilder.head(targetClass);
			if (sheet.excludes().length > 0) {
				writerSheetBuilder.excludeColumnFieldNames(Arrays.asList(sheet.excludes()));
			}
			if (sheet.includes().length > 0) {
				writerSheetBuilder.includeColumnFieldNames(Arrays.asList(sheet.includes()));
			}
		}

		// 用于spring bean自定义注入的增强器
		enhanceExcelWriterSheetBuilder(writerSheetBuilder, sheetNo, sheetName, targetClass, template,
				headGeneratorClass);
		// buidl
		return writerSheetBuilder.build();

	}

	/**
	 * 是否为Null Head Generator
	 * @param headGenerateClass
	 * @return
	 */
	private boolean isNotInterface(Class<? extends HeadGenerator> headGenerateClass) {
		return !Modifier.isInterface(headGenerateClass.getModifiers());
	}

	/**
	 * 自定义头部信息
	 * @param dataClass
	 * @param headEnhancerClass
	 * @param writerSheetBuilder
	 */
	private void fillCustomHeadInfo(Class<?> dataClass, Class<? extends HeadGenerator> headEnhancerClass,
			ExcelWriterSheetBuilder writerSheetBuilder) {
		// 必须要有一个Head头部信息生成器
		HeadGenerator headGenerator = this.applicationContext.getBean(headEnhancerClass);
		Assert.notNull(headGenerator, "The header generated bean does not exist.");
		HeadMeta head = headGenerator.head(dataClass);
		writerSheetBuilder.head(head.getHead());
		writerSheetBuilder.excludeColumnFieldNames(head.getIgnoreHeadFields());
	}

	/**
	 * 自定义注入转换器 如果有需要，子类自己重写
	 * @param builder
	 */
	private void registerCustomConverter(ExcelWriterBuilder builder) {
		converterProvider.ifAvailable(converters -> converters.forEach(builder::registerConverter));
	}

	/**
	 * 自定义注入增强器，增强ExcelWriterBuilder
	 * @param writerBuilder
	 * @param response
	 * @param responseExcel
	 * @param templatePath
	 */
	private void enhanceExcelWriterBuilder(ExcelWriterBuilder writerBuilder, HttpServletResponse response,
			ResponseExcel responseExcel, String templatePath) {
		enhancerProvider.ifAvailable(enhancers -> enhancers
				.forEach(e -> e.enhanceExcel(writerBuilder, response, responseExcel, templatePath)));
	}

	/**
	 * 自定义注入增强器，增强ExcelWriterSheetBuilder
	 * @param writerSheetBuilder
	 * @param sheetNo
	 * @param sheetName
	 * @param targetClass
	 * @param template
	 * @param headGeneratorClass
	 */
	private void enhanceExcelWriterSheetBuilder(ExcelWriterSheetBuilder writerSheetBuilder, Integer sheetNo,
			String sheetName, Class<?> targetClass, String template,
			Class<? extends HeadGenerator> headGeneratorClass) {
		enhancerProvider.ifAvailable(enhancers -> enhancers.forEach(e -> e.enhanceSheet(writerSheetBuilder, sheetNo,
				sheetName, targetClass, template, headGeneratorClass)));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
