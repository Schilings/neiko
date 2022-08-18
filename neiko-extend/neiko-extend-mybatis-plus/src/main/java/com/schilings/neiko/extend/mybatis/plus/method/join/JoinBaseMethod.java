package com.schilings.neiko.extend.mybatis.plus.method.join;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;

/**
 * <pre>
 * <p>{@link AbstractMethod}</p>
 * <p>Join 联表查询基础接口</p>
 * <p>from MPJ</p>
 * </pre>
 *
 * @author Schilings
 */
public interface JoinBaseMethod extends Constants {

	/**
	 * 参考{@link AbstractMethod#sqlWhereEntityWrapper(boolean, com.baomidou.mybatisplus.core.metadata.TableInfo)}<br>
	 * Where条件查询sql
	 * @param newLine
	 * @param table
	 * @return
	 */
	default String joinSqlWhereEntityWrapper(boolean newLine, TableInfo table) {
		// 如果存在表字段启用了逻辑删除，
		if (table.isWithLogicDelete()) {

			/**
			 * <script> <where> <choose> <when test="ew != nul"> AND ${ew.alias}.deleted =
			 * 'value' <if test="ew.sqlSegment != null and ew.sqlSegment != '' and
			 * nonEmptyOfNormal"> AND ${ew.sqlSegment} </if>
			 * <if test="ew.sqlSegment != null and ew.sqlSegment != '' and
			 * nonEmptyOfNormal"> ${ew.sqlSegment} </if> </when>
			 * <otherwise>${ew.alias}.deleted = 'value'</otherwise> </choose> </where>
			 * </script>
			 */
			String sqlScript = (NEWLINE + getLogicDeleteSql(table, true, true) + NEWLINE);
			String normalSqlScript = SqlScriptUtils.convertIf(String.format("AND ${%s}", WRAPPER_SQLSEGMENT),
					String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
							WRAPPER_NONEMPTYOFNORMAL),
					true);
			normalSqlScript += NEWLINE;
			normalSqlScript += SqlScriptUtils.convertIf(String.format(" ${%s}", WRAPPER_SQLSEGMENT),
					String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
							WRAPPER_EMPTYOFNORMAL),
					true);
			sqlScript += normalSqlScript;
			sqlScript = SqlScriptUtils.convertChoose(String.format("%s != null", WRAPPER), sqlScript,
					table.getLogicDeleteSql(false, true));
			sqlScript = SqlScriptUtils.convertWhere(sqlScript);
			return newLine ? NEWLINE + sqlScript : sqlScript;
		}
		else {
			/**
			 * <script> <if test="ew != nul"> <where>
			 * <if test="ew.sqlSegment != null and ew.sqlSegment != '' and
			 * nonEmptyOfNormal"> ${ew.sqlSegment} </if> </where>
			 * <if test="ew.sqlSegment != null and ew.sqlSegment != '' and
			 * nonEmptyOfNormal"> ${ew.sqlSegment} </if> </if> </script>
			 */
			String sqlScript = SqlScriptUtils.convertIf(String.format("${%s}", WRAPPER_SQLSEGMENT),
					String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
							WRAPPER_NONEMPTYOFWHERE),
					true);
			sqlScript = SqlScriptUtils.convertWhere(sqlScript) + NEWLINE;
			sqlScript += SqlScriptUtils.convertIf(String.format(" ${%s}", WRAPPER_SQLSEGMENT),
					String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
							WRAPPER_EMPTYOFWHERE),
					true);
			sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", WRAPPER), true);
			return newLine ? NEWLINE + sqlScript : sqlScript;
		}
	}

	/**
	 * 获取逻辑删除字段的 sql 脚本
	 * @param tableInfo
	 * @param startWithAnd
	 * @param isWhere
	 * @return
	 */
	default String getLogicDeleteSql(TableInfo tableInfo, boolean startWithAnd, boolean isWhere) {
		// 如果存在表字段启用了逻辑删除
		if (tableInfo.isWithLogicDelete()) {
			//
			String logicDeleteSql = formatLogicDeleteSql(tableInfo, isWhere);
			if (startWithAnd) {
				// AND .....
				logicDeleteSql = " AND " + logicDeleteSql;
			}
			return logicDeleteSql;
		}
		return EMPTY;
	}

	/**
	 * 取自MP的table.getLogicDeleteSql(false, true)，但加上${ew.alias}.
	 * @param tableInfo
	 * @param isWhere
	 * @return
	 */
	default String formatLogicDeleteSql(TableInfo tableInfo, boolean isWhere) {
		// 设定的逻辑删除或未删除标识值
		final String value = isWhere ? tableInfo.getLogicDeleteFieldInfo().getLogicNotDeleteValue()
				: tableInfo.getLogicDeleteFieldInfo().getLogicDeleteValue();
		// 是否isWhere，即是否未删除
		if (isWhere) {
			if (NULL.equalsIgnoreCase(value)) {
				// ${ew.alias}.deleted IS NULL
				return "${ew.alias}." + tableInfo.getLogicDeleteFieldInfo().getColumn() + " IS NULL";
			}
			else {
				// ${ew.alias}.deleted = 'value' 或 ${ew.alias}.deleted = value
				return "${ew.alias}." + tableInfo.getLogicDeleteFieldInfo().getColumn() + EQUALS
						+ String.format(tableInfo.getLogicDeleteFieldInfo().isCharSequence() ? "'%s'" : "%s", value);
			}
		}

		final String targetStr = "${ew.alias}." + tableInfo.getLogicDeleteFieldInfo().getColumn() + EQUALS;
		// ${ew.alias}.deleted = null
		if (NULL.equalsIgnoreCase(value)) {
			return targetStr + NULL;
		}
		else {
			// ${ew.alias}.deleted = 'value' 或 ${ew.alias}.deleted = value
			return targetStr
					+ String.format(tableInfo.getLogicDeleteFieldInfo().isCharSequence() ? "'%s'" : "%s", value);
		}
	}

	/**
	 * 去重
	 * @return
	 */
	default String joinSqlSelectColumns() {
		// <if test="ew.selectDistinct">DISTINCT</if>
		return SqlScriptUtils.convertIf("DISTINCT", "ew.selectDistinct", false);
	}

}
