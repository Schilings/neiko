package com.schilings.neiko.extend.mybatis.plus.wrapper.join;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.schilings.neiko.extend.mybatis.plus.constants.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <pre>
 *     Entity 对象封装操作类
 * <p>copy {@link QueryWrapper}</p>
 * </pre>
 *
 * @author Schilings
 */
public class NeikoQueryWrapper<T> extends AbstractWrapper<T, String, NeikoQueryWrapper<T>>
		implements Query<NeikoQueryWrapper<T>, T, String>,
		// 额外实现 NeikoJoin
		SqlJoin<NeikoQueryWrapper<T>> {

	/**
	 * 查询字段
	 * <p>
	 * copy {@link QueryWrapper}
	 * </p>
	 */
	private SharedString sqlSelect = new SharedString();

	/**
	 * 连表关键字段，如LEFT JOIN t_user t1 ON t1.id=t.user_id LEFT JOIN t_role t2 ON
	 * t2.id=t.role_id
	 */
	private SharedString from = SharedString.emptyString();

	/**
	 * 主表别名,默认为t
	 */
	private String alias = Constant.TABLE_ALIAS;

	/**
	 * 查询的列
	 */
	private List<String> selectColumns = new ArrayList<>();

	/**
	 * 排除的字段
	 */
	private List<String> ignoreColumns = new ArrayList<>();

	/**
	 * 是否 select distinct
	 */
	private boolean selectDistinct = false;

	public NeikoQueryWrapper() {
		super.initNeed();
	}

	/**
	 * 只用于生产嵌套 sql
	 * @param entityClass 本不应该需要的
	 */
	public NeikoQueryWrapper(T entity, Class<T> entityClass, AtomicInteger paramNameSeq,
			Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString sqlSelect,
			SharedString from, SharedString lastSql, SharedString sqlComment, SharedString sqlFirst,
			List<String> selectColumns, List<String> ignoreColumns) {
		super.setEntity(entity);
		super.setEntityClass(entityClass);
		this.paramNameSeq = paramNameSeq;
		this.paramNameValuePairs = paramNameValuePairs;
		this.expression = mergeSegments;
		this.sqlSelect = sqlSelect;
		this.lastSql = lastSql;
		this.from = from;
		this.sqlComment = sqlComment;
		this.sqlFirst = sqlFirst;
		this.selectColumns = selectColumns;
		this.ignoreColumns = ignoreColumns;
	}

	/**
	 * sql去重 select distinct
	 */
	public NeikoQueryWrapper<T> distinct() {
		this.selectDistinct = true;
		return typedThis;
	}

	/**
	 * 忽略查询字段
	 * <p>
	 * 用法: selectIgnore("t.id","t.sex","a.area")
	 *
	 * @since 1.1.3
	 */
	public NeikoQueryWrapper<T> selectIgnore(String... columns) {
		if (ArrayUtils.isNotEmpty(columns)) {
			// ["t.id","t.sex","a.area"]
			ignoreColumns.addAll(Arrays.asList(columns));
		}
		return typedThis;
	}

	@Override
	public NeikoQueryWrapper<T> select(String... columns) {
		if (ArrayUtils.isNotEmpty(columns)) {
			selectColumns.addAll(Arrays.asList(columns));
		}
		return typedThis;
	}

	/**
	 * 此方法只能用于主表 不含主键
	 * <p>
	 * 参考 from {@link QueryWrapper#select(java.lang.Class, java.util.function.Predicate)}
	 * </p>
	 * @param entityClass 主表class
	 * @param predicate 条件lambda
	 */
	@Override
	public NeikoQueryWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
		TableInfo info = TableInfoHelper.getTableInfo(entityClass);
		Assert.notNull(info, "can not find table info");
		// 查询的列：["t.column1","t.column2"]
		selectColumns.addAll(info.getFieldList().stream().filter(predicate)
				.map(c -> alias + StringPool.DOT + c.getColumn()).collect(Collectors.toList()));
		return typedThis;
	}

	/**
	 * 查询主表全部字段 含主键
	 * @param clazz 主表class
	 */
	public final NeikoQueryWrapper<T> selectAll(Class<T> clazz) {
		selectAll(clazz, alias);
		return typedThis;
	}

	/**
	 * 根据表别名查询指定实体全部字段
	 * @param as 实体对应的别名
	 */
	@SuppressWarnings({ "DuplicatedCode", "UnusedReturnValue" })
	public final NeikoQueryWrapper<T> selectAll(Class<?> clazz, String as) {
		TableInfo info = TableInfoHelper.getTableInfo(clazz);
		Assert.notNull(info, "can not find table info");
		// 若有主键
		if (info.havePK()) {
			// ["t.key"]
			selectColumns.add(as + StringPool.DOT + info.getKeyColumn());
		}
		// ["t.key","t.column1","t.column2"]
		selectColumns.addAll(info.getFieldList().stream().map(i -> as + StringPool.DOT + i.getColumn())
				.collect(Collectors.toList()));
		return typedThis;
	}

	/**
	 * 设置主表别名 如果要用，请最先调用，因为selectAll(Class)会用到主表别名，默认为t，后面更改了的话，会造成找不到别名为t的表 <pre> 正例 new
	 * QueryWrapper().setAlias("a").selectAll(UserDO.class).... 反例 new
	 * QueryWrapper().selectAll(UserDO.class).setAlias("a").... <pre/>
	 * @param alias 主表别名
	 */
	public NeikoQueryWrapper<T> setAlias(String alias) {
		Assert.isTrue(StringUtils.isNotBlank(alias), "别名不能为空");
		this.alias = alias;
		return this;
	}

	/**
	 * <p>
	 * {@link SqlJoin}
	 * </p>
	 * <p>
	 * 用于生成join sql
	 * </p>
	 * <p>
	 * 例如主表左联表1查询 leftJoin("t_user t1 on t1.id = t.user_id") --> left join t_user t1 on
	 * t1.id = t.user_id
	 * </p>
	 * @param keyWord
	 * @param condition
	 * @param joinSql
	 * @return
	 */
	@Override
	public NeikoQueryWrapper<T> join(String keyWord, boolean condition, String joinSql) {
		if (condition) {
			from.setStringValue(from.getStringValue() + keyWord + joinSql);
		}
		return typedThis;
	}

	/**
	 * 用于生成嵌套 sql
	 * <p>
	 * 故 sqlSelect selectColumn ignoreColumns from不向下传递
	 * </p>
	 */
	@Override
	protected NeikoQueryWrapper<T> instance() {
		return new NeikoQueryWrapper<>(getEntity(), getEntityClass(), paramNameSeq, paramNameValuePairs,
				new MergeSegments(), null, null, SharedString.emptyString(), SharedString.emptyString(),
				SharedString.emptyString(), null, null);
	}

	/**
	 * SQL 查询字段
	 * @return
	 */
	@Override
	public String getSqlSelect() {
		// 如果为空，拦截一下，把查询的字段放入
		if (StringUtils.isBlank(sqlSelect.getStringValue())) {
			if (CollectionUtils.isNotEmpty(ignoreColumns)) {
				// 去除忽略的列
				selectColumns.removeIf(ignoreColumns::contains);
			}
			// 在此时才落入SharedString,例如 "t.key,t.column1,t.column2"
			sqlSelect.setStringValue(String.join(StringPool.COMMA, selectColumns));
		}
		return sqlSelect.getStringValue();
	}

	public boolean getSelectDistinct() {
		return selectDistinct;
	}

	public String getFrom() {
		return from.getStringValue();
	}

	public String getAlias() {
		return alias;
	}

	@Override
	public void clear() {
		super.clear();
		sqlSelect.toNull();
		from.toNull();
		selectColumns.clear();
		ignoreColumns.clear();
	}

}
