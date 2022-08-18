package com.schilings.neiko.extend.mybatis.plus.method.join;

import com.baomidou.mybatisplus.core.injector.methods.SelectMaps;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.schilings.neiko.extend.mybatis.plus.constants.SqlMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

/**
 *
 * <p>
 * copy {@link SelectMaps}
 * </p>
 *
 * @author Schilings
 */
public class SelectJoinMapsList extends JoinAbstractMethod {

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {

		SqlMethod sqlMethod = SqlMethod.SELECT_JOIN_MAPS;
		String sql = String.format(sqlMethod.getSql(), sqlFirst(), sqlDistinct(), sqlSelectColumns(tableInfo, true),
				tableInfo.getTableName(), sqlAlias(), sqlFrom(), sqlWhereEntityWrapper(true, tableInfo), sqlComment());
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
		// 返回类型Map.class
		return this.addSelectMappedStatementForOther(mapperClass, sqlMethod.getMethod(), sqlSource, Map.class);
	}

}
