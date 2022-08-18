package com.schilings.neiko.extend.mybatis.plus.wrapper.join;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.schilings.neiko.extend.mybatis.plus.constants.BaseFuncEnum;
import com.schilings.neiko.extend.mybatis.plus.constants.DefaultFuncEnum;
import com.schilings.neiko.extend.mybatis.plus.utils.LambdaUtils;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * 参考 {@link com.baomidou.mybatisplus.core.conditions.query.Query}
 *
 * @author yulichang
 */
@SuppressWarnings("unused")
public interface Query<Children> extends Serializable {

	/**
	 * 设置查询字段
	 * @param columns 字段数组
	 * @return children
	 */
	@SuppressWarnings("unchecked")
	<E> Children select(SFunction<E, ?>... columns);

	/**
	 * 过滤查询的字段信息(主键除外!)
	 * <p>
	 * 例1: 只要 java 字段名以 "test" 开头的 -> select(i -> i.getProperty().startsWith("test"))
	 * </p>
	 * <p>
	 * 例2: 只要 java 字段属性是 CharSequence 类型的 -> select(TableFieldInfo::isCharSequence)
	 * </p>
	 * <p>
	 * 例3: 只要 java 字段没有填充策略的 -> select(i -> i.getFieldFill() == FieldFill.DEFAULT)
	 * </p>
	 * <p>
	 * 例4: 要全部字段 -> select(i -> true)
	 * </p>
	 * <p>
	 * 例5: 只要主键字段 -> select(i -> false)
	 * </p>
	 * @param predicate 过滤方式
	 * @return children
	 */
	<E> Children select(Class<E> entityClass, Predicate<TableFieldInfo> predicate);

	/**
	 * ignore
	 */
	default <S, X> Children selectAs(SFunction<S, ?> column, SFunction<X, ?> alias) {
		return selectAs(column, LambdaUtils.getName(alias));
	}

	/**
	 * 别名查询
	 */
	<S> Children selectAs(SFunction<S, ?> column, String alias);

	/**
	 * 聚合函数查询
	 * @param funcEnum 函数枚举 {@link DefaultFuncEnum}
	 * @param column 函数作用的字段
	 * @param alias 别名
	 */
	Children selectFunc(boolean condition, BaseFuncEnum funcEnum, Object column, String alias);

	<S> Children selectFunc(boolean condition, BaseFuncEnum funcEnum, SFunction<S, ?> column, String alias);

	default <S> Children selectFunc(BaseFuncEnum funcEnum, Object column, String alias) {
		return selectFunc(true, funcEnum, column, alias);
	}

	default <S> Children selectFunc(BaseFuncEnum funcEnum, SFunction<S, ?> column, String alias) {
		return selectFunc(true, funcEnum, column, alias);
	}

	default <S, X> Children selectFunc(BaseFuncEnum funcEnum, SFunction<S, ?> column, SFunction<X, ?> alias) {
		return selectFunc(true, funcEnum, column, LambdaUtils.getName(alias));
	}

	default <S> Children selectFunc(BaseFuncEnum funcEnum, SFunction<S, ?> column) {
		return selectFunc(true, funcEnum, column, column);
	}

	default <X> Children selectFunc(BaseFuncEnum funcEnum, Object column, SFunction<X, ?> alias) {
		return selectFunc(true, funcEnum, column, LambdaUtils.getName(alias));
	}

	default <S, X> Children selectFunc(boolean condition, BaseFuncEnum funcEnum, SFunction<S, ?> column,
			SFunction<X, ?> alias) {
		return selectFunc(condition, funcEnum, column, LambdaUtils.getName(alias));
	}

	default <S> Children selectFunc(boolean condition, BaseFuncEnum funcEnum, SFunction<S, ?> column) {
		return selectFunc(condition, funcEnum, column, column);
	}

	default <X> Children selectFunc(boolean condition, BaseFuncEnum funcEnum, Object column, SFunction<X, ?> alias) {
		return selectFunc(condition, funcEnum, column, LambdaUtils.getName(alias));
	}

	/**
	 * 忽略查询字段
	 * <p>
	 * 用法: selectIgnore(UserDO::getId,UserDO::getSex) 注意: 一个selectIgnore只支持一个对象
	 * 如果要忽略多个实体的字段,请调用多次
	 * <p>
	 * .selectIgnore(UserDO::getId,UserDO::getSex)
	 * .selectIgnore(UserAddressDO::getArea,UserAddressDO::getCity)
	 *
	 * @since 1.1.3
	 */
	@SuppressWarnings("unchecked")
	<S> Children selectIgnore(SFunction<S, ?>... columns);

	/**
	 * 查询实体类全部字段
	 */
	Children selectAll(Class<?> clazz);

	/**
	 * select sql 片段
	 */
	String getSqlSelect();

	/* 默认聚合函数扩展 */

	/**
	 * SUM()
	 */
	default <S> Children selectSum(SFunction<S, ?> column) {
		return selectFunc(DefaultFuncEnum.SUM, column);
	}

	default <S, X> Children selectSum(SFunction<S, ?> column, SFunction<X, ?> alias) {
		return selectFunc(DefaultFuncEnum.SUM, column, alias);
	}

	default <S, X> Children selectSum(SFunction<S, ?> column, String alias) {
		return selectFunc(DefaultFuncEnum.SUM, column, alias);
	}

	default <S> Children selectSum(boolean condition, SFunction<S, ?> column) {
		return selectFunc(condition, DefaultFuncEnum.SUM, column);
	}

	default <S, X> Children selectSum(boolean condition, SFunction<S, ?> column, SFunction<X, ?> alias) {
		return selectFunc(condition, DefaultFuncEnum.SUM, column, alias);
	}

	default <S, X> Children selectSum(boolean condition, SFunction<S, ?> column, String alias) {
		return selectFunc(condition, DefaultFuncEnum.SUM, column, alias);
	}

	/**
	 * COUNT()
	 */
	default <S> Children selectCount(SFunction<S, ?> column) {
		return selectFunc(DefaultFuncEnum.COUNT, column);
	}

	default <X> Children selectCount(Object column, SFunction<X, ?> alias) {
		return selectFunc(DefaultFuncEnum.COUNT, column, alias);
	}

	default Children selectCount(Object column, String alias) {
		return selectFunc(DefaultFuncEnum.COUNT, column, alias);
	}

	default <S, X> Children selectCount(SFunction<S, ?> column, SFunction<X, ?> alias) {
		return selectFunc(DefaultFuncEnum.COUNT, column, alias);
	}

	default <S, X> Children selectCount(SFunction<S, ?> column, String alias) {
		return selectFunc(DefaultFuncEnum.COUNT, column, alias);
	}

	default <S> Children selectCount(boolean condition, SFunction<S, ?> column) {
		return selectFunc(condition, DefaultFuncEnum.COUNT, column);
	}

	default <X> Children selectCount(boolean condition, Object column, SFunction<X, ?> alias) {
		return selectFunc(condition, DefaultFuncEnum.COUNT, column, alias);
	}

	default Children selectCount(boolean condition, Object column, String alias) {
		return selectFunc(condition, DefaultFuncEnum.COUNT, column, alias);
	}

	default <S, X> Children selectCount(boolean condition, SFunction<S, ?> column, SFunction<X, ?> alias) {
		return selectFunc(condition, DefaultFuncEnum.COUNT, column, alias);
	}

	default <S, X> Children selectCount(boolean condition, SFunction<S, ?> column, String alias) {
		return selectFunc(condition, DefaultFuncEnum.COUNT, column, alias);
	}

	/**
	 * MAX()
	 */
	default <S> Children selectMax(SFunction<S, ?> column) {
		return selectFunc(DefaultFuncEnum.MAX, column);
	}

	default <S, X> Children selectMax(SFunction<S, ?> column, SFunction<X, ?> alias) {
		return selectFunc(DefaultFuncEnum.MAX, column, alias);
	}

	default <S, X> Children selectMax(SFunction<S, ?> column, String alias) {
		return selectFunc(DefaultFuncEnum.MAX, column, alias);
	}

	default <S> Children selectMax(boolean condition, SFunction<S, ?> column) {
		return selectFunc(condition, DefaultFuncEnum.MAX, column);
	}

	default <S, X> Children selectMax(boolean condition, SFunction<S, ?> column, SFunction<X, ?> alias) {
		return selectFunc(condition, DefaultFuncEnum.MAX, column, alias);
	}

	default <S, X> Children selectMax(boolean condition, SFunction<S, ?> column, String alias) {
		return selectFunc(condition, DefaultFuncEnum.MAX, column, alias);
	}

	/**
	 * MIN()
	 */
	default <S> Children selectMin(SFunction<S, ?> column) {
		return selectFunc(DefaultFuncEnum.MIN, column);
	}

	default <S, X> Children selectMin(SFunction<S, ?> column, SFunction<X, ?> alias) {
		return selectFunc(DefaultFuncEnum.MIN, column, alias);
	}

	default <S, X> Children selectMin(SFunction<S, ?> column, String alias) {
		return selectFunc(DefaultFuncEnum.MIN, column, alias);
	}

	default <S> Children selectMin(boolean condition, SFunction<S, ?> column) {
		return selectFunc(condition, DefaultFuncEnum.MIN, column);
	}

	default <S, X> Children selectMin(boolean condition, SFunction<S, ?> column, SFunction<X, ?> alias) {
		return selectFunc(condition, DefaultFuncEnum.MIN, column, alias);
	}

	default <S, X> Children selectMin(boolean condition, SFunction<S, ?> column, String alias) {
		return selectFunc(condition, DefaultFuncEnum.MIN, column, alias);
	}

	/**
	 * MIN()
	 */
	default <S> Children selectAvg(SFunction<S, ?> column) {
		return selectFunc(DefaultFuncEnum.AVG, column);
	}

	default <S, X> Children selectAvg(SFunction<S, ?> column, SFunction<X, ?> alias) {
		return selectFunc(DefaultFuncEnum.AVG, column, alias);
	}

	default <S, X> Children selectAvg(SFunction<S, ?> column, String alias) {
		return selectFunc(DefaultFuncEnum.AVG, column, alias);
	}

	default <S> Children selectAvg(boolean condition, SFunction<S, ?> column) {
		return selectFunc(condition, DefaultFuncEnum.AVG, column);
	}

	default <S, X> Children selectAvg(boolean condition, SFunction<S, ?> column, SFunction<X, ?> alias) {
		return selectFunc(condition, DefaultFuncEnum.AVG, column, alias);
	}

	default <S, X> Children selectAvg(boolean condition, SFunction<S, ?> column, String alias) {
		return selectFunc(condition, DefaultFuncEnum.AVG, column, alias);
	}

	/**
	 * LEN()
	 */
	default <S> Children selectLen(SFunction<S, ?> column) {
		return selectFunc(DefaultFuncEnum.LEN, column);
	}

	default <S, X> Children selectLen(SFunction<S, ?> column, SFunction<X, ?> alias) {
		return selectFunc(DefaultFuncEnum.LEN, column, alias);
	}

	default <S, X> Children selectLen(SFunction<S, ?> column, String alias) {
		return selectFunc(DefaultFuncEnum.LEN, column, alias);
	}

	default <S> Children selectLen(boolean condition, SFunction<S, ?> column) {
		return selectFunc(condition, DefaultFuncEnum.LEN, column);
	}

	default <S, X> Children selectLen(boolean condition, SFunction<S, ?> column, SFunction<X, ?> alias) {
		return selectFunc(condition, DefaultFuncEnum.LEN, column, alias);
	}

	default <S, X> Children selectLen(boolean condition, SFunction<S, ?> column, String alias) {
		return selectFunc(condition, DefaultFuncEnum.LEN, column, alias);
	}

}
