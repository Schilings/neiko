package com.schilings.neiko.extend.mybatis.plus.wrapper.join;



import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.schilings.neiko.extend.mybatis.plus.constants.BaseFuncEnum;
import com.schilings.neiko.extend.mybatis.plus.constants.Constant;
import com.schilings.neiko.extend.mybatis.plus.utils.LambdaUtils;
import lombok.Data;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * 参考 {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper}
 * Lambda 语法使用 Wrapper
 * <p>
 */
public class NeikoLambdaQueryWrapper<T> 
        extends NeikoAbstractLambdaWrapper<T, NeikoLambdaQueryWrapper<T>> 
        implements Query<NeikoLambdaQueryWrapper<T>>, LambdaJoin<NeikoLambdaQueryWrapper<T>>{

    /**
     * 查询字段 sql
     */
    private SharedString sqlSelect = new SharedString();

    /**
     * 查询表
     */
    private final SharedString from = new SharedString();

    /**
     * 主表别名
     */
    private final SharedString alias = new SharedString(Constant.TABLE_ALIAS);

    /**
     * 查询的字段
     */
    private final List<SelectColumn> selectColumns = new ArrayList<>();

    /**
     * 忽略查询的字段
     */
    private final List<SelectColumn> ignoreColumns = new ArrayList<>();

    /**
     * 是否 select distinct
     */
    private boolean selectDistinct = false;

    /**
     * 表序号
     */
    private int tableIndex = 1;

    /**
     * ON sql wrapper集合
     */
    private final List<NeikoLambdaQueryWrapper<?>> onWrappers = new ArrayList<>();

    /**
     * 连表关键字 on 条件 func 使用
     */
    @Getter
    private String keyWord;

    /**
     * 连表实体类 on 条件 func 使用
     */
    @Getter
    private Class<?> joinClass;

    /**
     * 不建议直接 new 该实例
     */
    public NeikoLambdaQueryWrapper() {
        super.initNeed();
    }


    /**
     * 不建议直接 new 该实例
     */
    NeikoLambdaQueryWrapper(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
                     Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                     SharedString lastSql, SharedString sqlComment, SharedString sqlFirst,
                     Map<Class<?>, Integer> subTable, String keyWord, Class<?> joinClass) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
        this.subTable = subTable;
        this.keyWord = keyWord;
        this.joinClass = joinClass;
    }


    /**
     * sql去重
     * select distinct
     */
    public NeikoLambdaQueryWrapper<T> distinct() {
        this.selectDistinct = true;
        return typedThis;
    }

    @Override
    @SafeVarargs
    public final <S> NeikoLambdaQueryWrapper<T> select(SFunction<S, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            for (SFunction<S, ?> s : columns) {
                selectColumns.add(SelectColumn.of(com.schilings.neiko.extend.mybatis.plus.utils.LambdaUtils.getEntityClass(s), getCache(s).getColumn()));
            }
        }
        return typedThis;
    }

    @Override
    public <E> NeikoLambdaQueryWrapper<T> select(Class<E> entityClass, Predicate<TableFieldInfo> predicate) {
        TableInfo info = TableInfoHelper.getTableInfo(entityClass);
        Assert.notNull(info, "table can not be find");
        info.getFieldList().stream().filter(predicate).collect(Collectors.toList()).forEach(
                i -> selectColumns.add(SelectColumn.of(entityClass, i.getColumn())));
        return typedThis;
    }

    @Override
    public <S> NeikoLambdaQueryWrapper<T> selectAs(SFunction<S, ?> column, String alias) {
        selectColumns.add(SelectColumn.of(com.schilings.neiko.extend.mybatis.plus.utils.LambdaUtils.getEntityClass(column), getCache(column).getColumn(), alias));
        return typedThis;
    }

    public <S> NeikoLambdaQueryWrapper<T> selectFunc(boolean condition, BaseFuncEnum funcEnum, SFunction<S, ?> column, String alias) {
        if (condition) {
            selectColumns.add(SelectColumn.of(com.schilings.neiko.extend.mybatis.plus.utils.LambdaUtils.getEntityClass(column), getCache(column).getColumn(), alias, funcEnum));
        }
        return typedThis;
    }

    @Override
    public NeikoLambdaQueryWrapper<T> selectFunc(boolean condition, BaseFuncEnum funcEnum, Object column, String alias) {
        if (condition) {
            selectColumns.add(SelectColumn.of(null, column.toString(), alias, funcEnum));
        }
        return typedThis;
    }

    public final NeikoLambdaQueryWrapper<T> selectAll(Class<?> clazz) {
        TableInfo info = TableInfoHelper.getTableInfo(clazz);
        Assert.notNull(info, "table can not be find -> %s", clazz);
        if (info.havePK()) {
            selectColumns.add(SelectColumn.of(clazz, info.getKeyColumn()));
        }
        info.getFieldList().forEach(c ->
                selectColumns.add(SelectColumn.of(clazz, c.getColumn())));
        return typedThis;
    }

    @Override
    @SafeVarargs
    public final <S> NeikoLambdaQueryWrapper<T> selectIgnore(SFunction<S, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            for (SFunction<S, ?> s : columns) {
                ignoreColumns.add(SelectColumn.of(LambdaUtils.getEntityClass(s), getCache(s).getColumn()));
            }
        }
        return typedThis;
    }

    /**
     * 查询条件 SQL 片段
     */
    @Override
    public String getSqlSelect() {
        if (StringUtils.isBlank(sqlSelect.getStringValue())) {
            if (CollectionUtils.isNotEmpty(ignoreColumns)) {
                selectColumns.removeIf(c -> c.getFuncEnum() == null && ignoreColumns.stream().anyMatch(i ->
                        i.getClazz() == c.getClazz() && Objects.equals(c.getColumnName(), i.getColumnName())));
            }
            String s = selectColumns.stream().map(i -> {
                String str = Constant.TABLE_ALIAS + getDefault(subTable.get(i.getClazz())) + StringPool.DOT + i.getColumnName();
                return (i.getFuncEnum() == null ? str : String.format(i.getFuncEnum().getSql(), str)) +
                        (StringUtils.isBlank(i.getAlias()) ? StringPool.EMPTY : (Constant.AS + i.getAlias()));
            }).collect(Collectors.joining(StringPool.COMMA));
            sqlSelect.setStringValue(s);
        }
        return sqlSelect.getStringValue();
    }

    /**
     * 获取连表部分语句
     */
    public String getFrom() {
        if (StringUtils.isBlank(from.getStringValue())) {
            StringBuilder value = new StringBuilder();
            for (NeikoLambdaQueryWrapper<?> wrapper : onWrappers) {
                String tableName = TableInfoHelper.getTableInfo(wrapper.getJoinClass()).getTableName();
                value.append(wrapper.getKeyWord())
                        .append(tableName)
                        .append(Constant.SPACE_TABLE_ALIAS)
                        .append(subTable.get(wrapper.getJoinClass()))
                        .append(Constant.ON)
                        .append(wrapper.getExpression().getNormal().getSqlSegment());
            }
            from.setStringValue(value.toString());
        }
        return from.getStringValue();
    }

    public String getAlias() {
        return alias.getStringValue();
    }


    public boolean getSelectDistinct() {
        return selectDistinct;
    }

    /**
     * 用于生成嵌套 sql
     * <p>故 sqlSelect 不向下传递</p>
     */
    @Override
    protected NeikoLambdaQueryWrapper<T> instance() {
        return instance(null, null);
    }

    protected NeikoLambdaQueryWrapper<T> instance(String keyWord, Class<?> joinClass) {
        return new NeikoLambdaQueryWrapper<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
                new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(),
                this.subTable, keyWord, joinClass);
    }

    @Override
    public void clear() {
        super.clear();
        sqlSelect.toNull();
        from.toNull();
        selectColumns.clear();
        ignoreColumns.clear();
        subTable.clear();
    }

    @Override
    public <R> NeikoLambdaQueryWrapper<T> join(String keyWord, boolean condition, Class<R> clazz, OnFunction function) {
        if (condition) {
            NeikoLambdaQueryWrapper<?> apply = function.apply(instance(keyWord, clazz));
            onWrappers.add(apply);
            subTable.put(clazz, tableIndex);
            tableIndex++;
        }
        return typedThis;
    }
    
    
    /**
     * select字段
     */
    @Data
    private static class SelectColumn {

        /**
         * 字段实体类
         */
        private Class<?> clazz;

        /**
         * 数据库字段名
         */
        private String columnName;

        /**
         * 字段别名
         */
        private String alias;

        /**
         * 字段函数
         */
        private BaseFuncEnum funcEnum;

        /**
         * 自定义函数填充参数
         */
        private List<SFunction<?, ?>> funcArgs;

        private SelectColumn(Class<?> clazz, String columnName, String alias, BaseFuncEnum funcEnum) {
            this.clazz = clazz;
            this.columnName = columnName;
            this.alias = alias;
            this.funcEnum = funcEnum;
        }

        public static SelectColumn of(Class<?> clazz, String columnName) {
            return new SelectColumn(clazz, columnName, null, null);
        }

        public static SelectColumn of(Class<?> clazz, String columnName, String alias) {
            return new SelectColumn(clazz, columnName, alias, null);
        }

        public static SelectColumn of(Class<?> clazz, String columnName, String alias, BaseFuncEnum funcEnum) {
            return new SelectColumn(clazz, columnName, alias, funcEnum);
        }
    }
}
