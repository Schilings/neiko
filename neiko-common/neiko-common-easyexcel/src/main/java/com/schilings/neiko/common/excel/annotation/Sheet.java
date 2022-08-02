package com.schilings.neiko.common.excel.annotation;

import com.schilings.neiko.common.excel.head.HeadGenerator;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sheet {

	int sheetNo() default -1;

	/**
	 * sheet name
	 */
	String sheetName();

	/**
	 * 包含字段
	 */
	String[] includes() default {};

	/**
	 * 排除字段
	 */
	String[] excludes() default {};

	/**
	 * 头生成器,不同的sheet可能有不同的头部策略,注意，配置的该类型一定要在spring容器中存在实例
	 */
	Class<? extends HeadGenerator> headGeneratorClass() default HeadGenerator.class;

}
