package com.schilings.neiko.samples.starter.datascope.sql;

import com.schilings.neiko.common.datascope.DataScope;
import com.schilings.neiko.common.datascope.core.DataPermissionHandler;
import com.schilings.neiko.common.datascope.core.DefaultDataPermissionHandler;
import com.schilings.neiko.common.datascope.holder.DataScopeMatchNumHolder;
import com.schilings.neiko.common.datascope.sql.DataScopeSqlProcessor;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

class DataScopeMatchTest {

	DataScope dataScope = new DataScope() {

		final String columnId = "order_id";

		@Override
		public String getResource() {
			return "order";
		}

		@Override
		public Collection<String> getTableNames() {
			Set<String> tableNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
			tableNames.addAll(Arrays.asList("t_order", "t_order_info"));
			return tableNames;
		}

		@Override
		public Expression getExpression(String tableName, Alias tableAlias) {
			Column column = new Column(tableAlias == null ? columnId : tableAlias.getName() + "." + columnId);
			ExpressionList expressionList = new ExpressionList();
			expressionList.setExpressions(Arrays.asList(new StringValue("1"), new StringValue("2")));
			return new InExpression(column, expressionList);
		}
	};

	DataPermissionHandler dataPermissionHandler = new DefaultDataPermissionHandler(
			Collections.singletonList(dataScope));

	DataScopeSqlProcessor dataScopeSqlProcessor = new DataScopeSqlProcessor();

	@Test
	void testMatchNum() {
		String sql = "select o.order_id,o.order_name,oi.order_price "
				+ "from t_ORDER o left join t_order_info oi on o.order_id = oi.order_id "
				+ "where oi.order_price > 100";

		DataScopeMatchNumHolder.initMatchNum();
		try {
			String parseSql = dataScopeSqlProcessor.parserSingle(sql, dataPermissionHandler.dataScopes());
			System.out.println(parseSql);

			Integer matchNum = DataScopeMatchNumHolder.pollMatchNum();
			Assertions.assertEquals(2, matchNum, "sql 数据权限匹配计数异常");
		}
		finally {
			DataScopeMatchNumHolder.removeIfEmpty();
		}

	}

	@Test
	void testNoMatch() {
		String sql = "select o.order_id,o.order_name,oi.order_price "
				+ "from t_ORDER_1 o left join t_order_info_1 oi on o.order_id = oi.order_id "
				+ "where oi.order_price > 100";
		DataScopeMatchNumHolder.initMatchNum();
		try {
			String parseSql = dataScopeSqlProcessor.parserSingle(sql, dataPermissionHandler.dataScopes());
			System.out.println(parseSql);

			Integer matchNum = DataScopeMatchNumHolder.pollMatchNum();
			Assertions.assertEquals(0, matchNum, "sql 数据权限匹配计数异常");

		}
		finally {
			DataScopeMatchNumHolder.removeIfEmpty();
		}

	}

	/**
	 * 嵌套进行 matchNumber 匹配
	 */
	@Test
	void testNestedMatchNum() {
		String sql = "select o.order_id,o.order_name,oi.order_price "
				+ "from t_ORDER o left join t_order_info oi on o.order_id = oi.order_id "
				+ "where oi.order_price > 100";

		DataScopeMatchNumHolder.initMatchNum();
		try {

			testNoMatch();

			String parseSql = dataScopeSqlProcessor.parserSingle(sql, dataPermissionHandler.dataScopes());
			System.out.println(parseSql);

			Integer matchNum = DataScopeMatchNumHolder.pollMatchNum();
			Assertions.assertEquals(2, matchNum, "sql 数据权限匹配计数异常");

		}
		finally {
			DataScopeMatchNumHolder.removeIfEmpty();
		}

	}

}
