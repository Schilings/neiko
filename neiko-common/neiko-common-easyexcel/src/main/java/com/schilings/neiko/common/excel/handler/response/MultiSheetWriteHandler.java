package com.schilings.neiko.common.excel.handler.response;


import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.schilings.neiko.common.excel.annotation.ResponseExcel;
import com.schilings.neiko.common.excel.annotation.Sheet;
import com.schilings.neiko.common.excel.handler.response.enhancer.WriterBuilderEnhancer;
import com.schilings.neiko.common.excel.vo.ExcelException;
import com.schilings.neiko.common.excel.properties.ExcelConfigProperties;
import org.springframework.beans.factory.ObjectProvider;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <pre>
 * <p>针对单个sheet的Excel导出</p>
 * </pre>
 * @author Schilings
*/
public class MultiSheetWriteHandler extends AbstractSheetWriteHandler{
    public MultiSheetWriteHandler(ObjectProvider<List<Converter<?>>> converterProvider, ObjectProvider<List<WriterBuilderEnhancer>> enhancerProvider, ExcelConfigProperties excelConfigProperties) {
        super(converterProvider, enhancerProvider, excelConfigProperties);
    }

    /**
     * 当且仅当List不为空且List中的元素也是List 才返回true
     * @param obj
     * @return
     */
    @Override
    public boolean support(Object obj) {
        if (obj instanceof List) {
            List<?> objList = (List<?>) obj;
            return !objList.isEmpty() && objList.get(0) instanceof List;
        }
        else {
            throw new ExcelException("@ResponseExcel 返回值必须为List类型");
        }
    }

    @Override
    public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
        List<?> objList = (List<?>) obj;
        ExcelWriter excelWriter = getExcelWriter(response, responseExcel);
        Sheet[] sheets = responseExcel.sheets();
        WriteSheet sheet;
        for (int i = 0; i < sheets.length; i++) {
            List<?> eleList = (List<?>) objList.get(i);
            Class<?> dataClass = eleList.get(0).getClass();
            // 创建sheet
            sheet = this.sheet(sheets[i], dataClass, responseExcel.template(), responseExcel.headGenerator());
            // 写入sheet
            excelWriter.write(eleList, sheet);
        }
        excelWriter.finish();
    }
}
