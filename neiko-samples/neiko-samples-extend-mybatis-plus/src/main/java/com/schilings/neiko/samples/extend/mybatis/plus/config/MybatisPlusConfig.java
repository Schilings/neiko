package com.schilings.neiko.samples.extend.mybatis.plus.config;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.schilings.neiko.samples.extend.mybatis.plus.dto.EntirePreorders;
import com.schilings.neiko.samples.extend.mybatis.plus.dto.PreordersGasStationDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

//@Configuration
@Slf4j
public class MybatisPlusConfig {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	/**
	 * @AutoResultMap注解的实体类自动构建resultMap并注入
	 */
	@PostConstruct
	public void initAutoResultMap() {
		try {
			log.info("--- start register @AutoResultMap ---");

			String namespace = "auto";

			// String packageName = "com.schilings.neiko.samples.extend.mybatis.plus.dto";
			// Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(packageName,
			// AutoResultMap.class);

			Set<Class<?>> classes = new HashSet<>();
			classes.add(EntirePreorders.class);
			org.apache.ibatis.session.Configuration configuration = sqlSessionTemplate.getConfiguration();

			for (Class clazz : classes) {
				MapperBuilderAssistant assistant = new MapperBuilderAssistant(configuration, "");
				assistant.setCurrentNamespace(namespace);
				TableInfo tableInfo = TableInfoHelper.initTableInfo(assistant, clazz);

				if (!tableInfo.isAutoInitResultMap()) {
					// 设置 tableInfo的autoInitResultMap属性 为 true
					ReflectUtil.setFieldValue(tableInfo, "autoInitResultMap", true);
					// 调用 tableInfo#initResultMapIfNeed() 方法，自动构建 resultMap 并注入
					ReflectUtil.invoke(tableInfo, "initResultMapIfNeed");
				}
			}

			log.info("--- finish register @AutoResultMap ---");
		}
		catch (Throwable e) {
			log.error("initAutoResultMap error", e);
			System.exit(1);
		}
	}

}
