package com.schilings.neiko.samples.extend.mybatis.plus.datascope;


import com.schilings.neiko.common.datascope.DataScope;
import com.schilings.neiko.common.datascope.util.SqlParseUtils;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

@Component
public class CustomDataScope implements DataScope {

    public static final String RESOURCE_NAME = "tenant";

    final String columnName = "deleted";
    
    @Override
    public String getResource() {
        return RESOURCE_NAME;
    }

    @Override
    public Collection<String> getTableNames() {
        Set<String> tableNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        tableNames.addAll(Arrays.asList("preorders","gas_station"));
        return tableNames;
    }

    @Override
    public Expression getExpression(String tableName, Alias tableAlias) {
        Column column = SqlParseUtils.getAliasColumn(tableName, tableAlias, columnName);
        return new EqualsTo(column, new LongValue("1"));
    }
}
