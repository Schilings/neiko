package com.schilings.neiko.common.excel.handler.response.parser;


import com.schilings.neiko.common.excel.annotation.ExcelProperty;
import com.schilings.neiko.common.util.reflect.ReflectUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * <p>ExcelProperties注解解析</p>
 * </pre>
 * @author Schilings
*/
@Deprecated
public class ExcelPropertyCachedParser {
    
    private static Map<Class<?>, List<ExcelPropertyMeta>> EXCEL_FIELD_CACHE = new ConcurrentHashMap<>();

    /**
     * 解析ExcelProperty注释
     * @param targetClass
     */
    public List<ExcelPropertyMeta> parse(Class<?> targetClass) {
        List<ExcelPropertyMeta> cacheList = EXCEL_FIELD_CACHE.get(targetClass);
        if (cacheList == null) {
            Field[] fields = ReflectUtils.getFields(targetClass);
            ArrayList<ExcelPropertyMeta> metaList = new ArrayList<>();
            for (Field field : fields) {
                ExcelProperty excelProperty = AnnotatedElementUtils.getMergedAnnotation(field, ExcelProperty.class);
                if (excelProperty == null) {
                    //忽略无注释字段
                    continue;
                }
                Assert.notNull(excelProperty, "@ExcelProperty in " + field.getName() + " should not be null");
                metaList.add(createPropertyMeta(excelProperty, field));
            }
            EXCEL_FIELD_CACHE.put(targetClass, metaList);
            return metaList;
        } else {
            return cacheList;
        } 
    }

    private ExcelPropertyMeta createPropertyMeta(ExcelProperty excelProperty,Field field) {
        ExcelPropertyMeta meta = new ExcelPropertyMeta();
        meta.setField(field);
        meta.setFielName(field.getName());
        //是否忽略
        if (excelProperty.ignoreHead()) {
            meta.setIgnoreHead(true);
            return meta;
        }
        //无配置标题,默认属性名
        String[] head = excelProperty.head();
        if (head.length != 0) {
            meta.setHead(Arrays.asList(head));
        } else {
            meta.setHead(Arrays.asList(field.getName()));
        }
        return meta;
    }
    

}
