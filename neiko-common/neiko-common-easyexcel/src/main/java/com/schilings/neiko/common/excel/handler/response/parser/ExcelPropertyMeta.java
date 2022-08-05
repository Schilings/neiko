package com.schilings.neiko.common.excel.handler.response.parser;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Deprecated
@Data
public class ExcelPropertyMeta {

	private Field field;

	private String fielName;

	/**
	 * 忽略对应字段名称
	 */
	boolean ignoreHead;

	/**
	 * <p>
	 * 自定义头部信息
	 * </p>
	 */
	private List<String> head;

}
