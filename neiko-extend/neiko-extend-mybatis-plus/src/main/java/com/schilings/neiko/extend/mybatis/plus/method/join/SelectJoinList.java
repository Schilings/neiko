package com.schilings.neiko.extend.mybatis.plus.method.join;

import com.baomidou.mybatisplus.core.injector.methods.SelectMaps;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.schilings.neiko.extend.mybatis.plus.constants.SqlMethod;
import com.schilings.neiko.extend.mybatis.plus.method.JoinResultType;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;

/**
 * <pre>
 * <p>copy {@link SelectMaps}</p>
 * </pre>
 *
 * @author Schilings
 */
public class SelectJoinList extends JoinAbstractMethod {

	public SelectJoinList() {
		super(SqlMethod.SELECT_JOIN_LIST.getMethod());
	}

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		/**
		 * <script> <if test="ew != null and ew.sqlFirst != null"> ${ew.sqlFirst} </if>
		 * SELECT <if test="ew.selectDistinct">DISTINCT</if> <choose> <when test="ew
		 * !=null and ew.sqlSelect != null> ${ew.sqlSelect} </when>
		 * <otherwize>t.field1,t.field2,....</otherwize> </choose> FROM tableName
		 * <if test="ew.alias != null and ew.alias != ''">${ew.alias}</if>
		 * <if test="ew.from != null and ew.from != ''">${ew.from}</if>
		 * <if test="ew != nul"> <where>
		 * <if test="ew.sqlSegment != null and ew.sqlSegment != '' and nonEmptyOfNormal">
		 * ${ew.sqlSegment} </if> </where>
		 * <if test="ew.sqlSegment != null and ew.sqlSegment != '' and nonEmptyOfNormal">
		 * ${ew.sqlSegment} </if> </if> <if test="ew != null and ew.sqlComment != null">
		 * ${ew.sqlComment} </if> </script>
		 */

		SqlMethod sqlMethod = SqlMethod.SELECT_JOIN_LIST;
		// sqlFirst()来自MP
		String sql = String.format(sqlMethod.getSql(), sqlFirst(), sqlDistinct(), sqlSelectColumns(tableInfo, true),
				tableInfo.getTableName(), sqlAlias(), sqlFrom(), sqlWhereEntityWrapper(true, tableInfo), sqlComment());
		// 这里用modelClass
		SqlSource sqlSource = languageDriver.createSqlSource(this.configuration, sql, modelClass);
		// 这里结果类型不是modelClass，而是自定义JoinResultType.class
		return this.addSelectMappedStatementForOther(mapperClass, sqlMethod.getMethod(), sqlSource,
				JoinResultType.class);
	}

}
