package com.schilings.neiko.common.excel.handler.response;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.schilings.neiko.common.excel.annotation.ResponseExcel;
import com.schilings.neiko.common.excel.handler.response.enhancer.WriterBuilderEnhancer;
import com.schilings.neiko.common.excel.vo.ExcelException;
import com.schilings.neiko.common.excel.properties.ExcelConfigProperties;
import org.springframework.beans.factory.ObjectProvider;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <pre>
 * <p>针对单个sheet的Excel导出</p>
 * </pre>
 *
 * @author Schilings
 */
public class SingleSheetWriteHandler extends AbstractSheetWriteHandler {

	public SingleSheetWriteHandler(ObjectProvider<List<Converter<?>>> converterProvider,
			ObjectProvider<List<WriterBuilderEnhancer>> enhancerProvider, ExcelConfigProperties excelConfigProperties) {
		super(converterProvider, enhancerProvider, excelConfigProperties);
	}

	/**
	 * obj 是List 且list不为空同时list中的元素不是是List 才返回true
	 * @param obj
	 * @return
	 */
	@Override
	public boolean support(Object obj) {
		if (obj instanceof List) {
			List<?> objList = (List<?>) obj;
			return !objList.isEmpty() && !(objList.get(0) instanceof List);
		}
		else {
			throw new ExcelException("@ResponseExcel 返回值必须为List类型");
		}
	}

	/**
	 * 写入响应流
	 * @param o obj
	 * @param response 输出对象
	 * @param responseExcel 注解
	 */
	@Override
	public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
		List<?> list = (List<?>) obj;
		ExcelWriter excelWriter = getExcelWriter(response, responseExcel);

		// 有模板则不指定sheet名
		Class<?> dataClass = list.get(0).getClass();
		WriteSheet sheet = this.sheet(responseExcel.sheets()[0], dataClass, responseExcel.template(),
				responseExcel.headGenerator());
		excelWriter.write(list, sheet);
		excelWriter.finish();
	}

}
