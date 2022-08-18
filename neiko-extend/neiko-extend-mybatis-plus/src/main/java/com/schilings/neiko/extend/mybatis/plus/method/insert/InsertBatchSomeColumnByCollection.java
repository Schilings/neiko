package com.schilings.neiko.extend.mybatis.plus.method.insert;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;
import java.util.function.Predicate;

/**
 * <pre>
 * <p>{@link InsertBatchSomeColumn}</p>
 * <p>更改为collection</p>
 * </pre>
 *
 * @author Schilings
 */
public class InsertBatchSomeColumnByCollection extends AbstractMethod {

	/**
	 * 字段筛选条件
	 */
	@Setter
	@Accessors(chain = true)
	private Predicate<TableFieldInfo> predicate;

	/**
	 * 默认方法名
	 */
	public InsertBatchSomeColumnByCollection() {
		super("insertBatchSomeColumn");
	}

	/**
	 * 默认方法名
	 * @param predicate 字段筛选条件
	 */
	public InsertBatchSomeColumnByCollection(Predicate<TableFieldInfo> predicate) {
		super("insertBatchSomeColumn");
		this.predicate = predicate;
	}

	/**
	 * @param name 方法名
	 * @param predicate 字段筛选条件
	 * @since 3.5.0
	 */
	public InsertBatchSomeColumnByCollection(String name, Predicate<TableFieldInfo> predicate) {
		super(name);
		this.predicate = predicate;
	}

	@SuppressWarnings("Duplicates")
	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		// 表包含主键处理逻辑,如果不包含主键当普通字段处理,havePK():主键是否为空
		KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
		String keyProperty = null;
		String keyColumn = null;
		if (tableInfo.havePK()) {
			if (tableInfo.getIdType() == IdType.AUTO) {
				/* 自增主键 */
				keyGenerator = Jdbc3KeyGenerator.INSTANCE;
				keyProperty = tableInfo.getKeyProperty();
				keyColumn = tableInfo.getKeyColumn();
			}
			else {
				if (null != tableInfo.getKeySequence()) {
					keyGenerator = TableInfoHelper.genKeyGenerator(this.methodName, tableInfo, builderAssistant);
					keyProperty = tableInfo.getKeyProperty();
					keyColumn = tableInfo.getKeyColumn();
				}
			}
		}
		// <script>
		// INSERT INTO %s %s VALUES %s
		// </script>
		SqlMethod sqlMethod = SqlMethod.INSERT_ONE;
		// 所有字段信息
		List<TableFieldInfo> fieldList = tableInfo.getFieldList();
		// 正常：主键,字段1,字段2,...,
		// 批量插入且主键标记为MP自增时：字段1,字段2,...,
		String insertSqlColumn = tableInfo.getKeyInsertSqlColumn(true, false)
				+ this.filterTableFieldInfo(fieldList, predicate, TableFieldInfo::getInsertSqlColumn, EMPTY);
		// (字段1,字段2,...)
		String columnScript = LEFT_BRACKET + insertSqlColumn.substring(0, insertSqlColumn.length() - 1) + RIGHT_BRACKET;
		// 正常：et.主键值,et.字段1值,et.字段2值,et.字段3值,
		// 批量插入且主键标记为MP自增时：et.字段1值,et.字段2值,et.字段3值,
		String insertSqlProperty = tableInfo.getKeyInsertSqlProperty(true, ENTITY_DOT, false)
				+ this.filterTableFieldInfo(fieldList, predicate, i -> i.getInsertSqlProperty(ENTITY_DOT), EMPTY);
		// (et.字段1值,et.字段2值,et.字段3值...)
		insertSqlProperty = LEFT_BRACKET + insertSqlProperty.substring(0, insertSqlProperty.length() - 1)
				+ RIGHT_BRACKET;
		// <foreach collection="collection" item="et" separator=",">
		// (et.字段1值,et.字段2值,et.字段3值...)
		// </foreach>
		String valuesScript = SqlScriptUtils.convertForeach(insertSqlProperty, "collection", null, ENTITY, COMMA);

		// 生成最终sql
		String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), columnScript, valuesScript);
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
		//
		return this.addInsertMappedStatement(mapperClass, modelClass, this.methodName, sqlSource, keyGenerator,
				keyProperty, keyColumn);
	}

}
