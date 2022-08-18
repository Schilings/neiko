package com.schilings.neiko.extend.mybatis.plus.method.insert;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.function.Predicate;

/**
 * <pre>
 * <p>批量插入，id重复则更新</p>
 * </pre>
 *
 * @author Schilings
 */
public class InsertOrUpdateBatch extends BaseInsertBatch {

	public InsertOrUpdateBatch() {
		super("insertOrUpdateBatch");
	}

	public InsertOrUpdateBatch(Predicate<TableFieldInfo> predicate) {
		super("insertOrUpdateBatch");
		this.predicate = predicate;
	}

	public InsertOrUpdateBatch(String methodName, Predicate<TableFieldInfo> predicate) {
		super(methodName);
		this.predicate = predicate;
	}

	/**
	 * 字段筛选条件
	 */
	@Setter
	@Accessors(chain = true)
	private Predicate<TableFieldInfo> predicate;

	/**
	 * <script>INSERT INTO %s %s VALUES %s</script>
	 * @return
	 */
	@Override
	protected String sql() {
		return SqlMethod.INSERT_ONE.getSql();
	}

	/**
	 * <script> INSERT INTO table field1,field2,field3 VALUES
	 * <foreach collection="collection" item="item" index="index" open="(" sparator="),("
	 * close=")>"> #{item.field1},#{item.field2},#{item.field3} </foreach> ON DUPLICATE
	 * KEY UPDATE field1=VALUES(field1),field2=VALUES(field2) <if test="!ignore">
	 * ,field2=VALUES(field2) </if> </script>
	 */
	@Override
	protected String prepareValuesSqlForMysqlBatch(TableInfo tableInfo) {
		// foreach
		StringBuilder sql = super.prepareValuesBuildSqlForMysqlBatch(tableInfo);
		// 添加重复策略
		sql.append(" ON DUPLICATE KEY UPDATE ");
		StringBuilder ignore = new StringBuilder();
		// 表字段，不包含id
		tableInfo.getFieldList().forEach(field -> {
			// 默认忽略逻辑删除字段
			if (!field.isLogicDelete()) {
				// 默认忽略字段
				if (!predicate.test(field)) {
					sql.append(field.getColumn()).append("=").append("VALUES(").append(field.getColumn()).append("),");
				}
				else {
					ignore.append(",").append(field.getColumn()).append("=").append("VALUES(").append(field.getColumn())
							.append(")");
				}
			}
		});

		// 删除最后一个多余的逗号
		sql.delete(sql.length() - 1, sql.length());

		// 配置不忽略全局配置字段时的sql部分
		sql.append("<if test=\"!ignore\">").append(ignore).append("</if>");
		return sql.toString();
	}

}
