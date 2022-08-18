package com.schilings.neiko.extend.mybatis.plus.utils;

import com.schilings.neiko.common.util.spring.SpringUtils;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionTemplate;

public final class SqlUtils {

	private static SqlSessionTemplate sqlSessionTemplate;

	static {
		sqlSessionTemplate = SpringUtils.getBean(SqlSessionTemplate.class);
	}

	public static Configuration getConfiguration() {
		return sqlSessionTemplate.getConfiguration();
	}

}
