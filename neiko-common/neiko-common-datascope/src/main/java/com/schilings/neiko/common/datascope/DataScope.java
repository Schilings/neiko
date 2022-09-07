package com.schilings.neiko.common.datascope;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;

import java.util.Collection;

/**
 *
 * <p>
 * {@link com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler}
 * </p>
 * <p>
 * {@link com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler}
 * </p>
 *
 * @author Schilings
 */
public interface DataScope {

	/**
	 * 数据所对应的资源
	 * @return 资源标识
	 */
	String getResource();

	/**
	 * 该资源相关的所有表，推荐使用 Set 类型。 <br/>
	 * 如需忽略表名大小写判断，则可以使用 TreeSet，并设置忽略大小写的自定义Comparator。 <br/>
	 * eg. new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
	 * @return tableNames
	 */
	Collection<String> getTableNames();

	/**
	 * 根据表名和表别名，动态生成的 where/or 筛选条件
	 * <p>
	 * 注意，返回null即表示对于该sql不做权限控制
	 * </p>
	 * @param tableName 表名
	 * @param tableAlias 表别名，可能为空
	 * @return 数据规则表达式
	 */
	Expression getExpression(String tableName, Alias tableAlias);

}
