package com.schilings.neiko.extend.mabatis.plus.wrapper.query;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * <pre>
 * <p>{@link LambdaQueryWrapper}</p>
 * <p>增加了一些简单条件的 IfPresent 条件 支持，Collection String Object 等等判断是否为空，或者是否为null</p>
 * </pre>
 * @author Schilings
*/
public class LambdaQueryWrapperX<T> extends AbstractLambdaWrapper<T, LambdaQueryWrapperX<T>>
        implements Query<LambdaQueryWrapperX<T>, T, SFunction<T, ?>> {

    
    //copy from mybatis plus：LambdaQueryWrapper-----------------begin
    /**
     * 查询字段
     */
    private SharedString sqlSelect = new SharedString();

    public LambdaQueryWrapperX() {
        this((T) null);
    }

    public LambdaQueryWrapperX(T entity) {
        super.setEntity(entity);
        super.initNeed();
    }

    public LambdaQueryWrapperX(Class<T> entityClass) {
        super.setEntityClass(entityClass);
        super.initNeed();
    }

    LambdaQueryWrapperX(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
                       Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString paramAlias,
                       SharedString lastSql, SharedString sqlComment, SharedString sqlFirst) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.paramAlias = paramAlias;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
    }

    /**
     * SELECT 部分 SQL 设置
     *
     * @param columns 查询字段
     */
    @SafeVarargs
    @Override
    public final LambdaQueryWrapperX<T> select(SFunction<T, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            this.sqlSelect.setStringValue(columnsToString(false, columns));
        }
        return typedThis;
    }

    /**
     * 过滤查询的字段信息(主键除外!)
     * <p>例1: 只要 java 字段名以 "test" 开头的             -> select(i -&gt; i.getProperty().startsWith("test"))</p>
     * <p>例2: 只要 java 字段属性是 CharSequence 类型的     -> select(TableFieldInfo::isCharSequence)</p>
     * <p>例3: 只要 java 字段没有填充策略的                 -> select(i -&gt; i.getFieldFill() == FieldFill.DEFAULT)</p>
     * <p>例4: 要全部字段                                   -> select(i -&gt; true)</p>
     * <p>例5: 只要主键字段                                 -> select(i -&gt; false)</p>
     *
     * @param predicate 过滤方式
     * @return this
     */
    @Override
    public LambdaQueryWrapperX<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        if (entityClass == null) {
            entityClass = getEntityClass();
        } else {
            setEntityClass(entityClass);
        }
        Assert.notNull(entityClass, "entityClass can not be null");
        this.sqlSelect.setStringValue(TableInfoHelper.getTableInfo(entityClass).chooseSelect(predicate));
        return typedThis;
    }

    @Override
    public String getSqlSelect() {
        return sqlSelect.getStringValue();
    }

    /**
     * 用于生成嵌套 sql
     * <p>故 sqlSelect 不向下传递</p>
     */
    @Override
    protected LambdaQueryWrapperX<T> instance() {
        return new LambdaQueryWrapperX<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
                new MergeSegments(), paramAlias, SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString());
    }

    @Override
    public void clear() {
        super.clear();
        sqlSelect.toNull();
    }

    //copy from mybatis plus：LambdaQueryWrapper-----------------end
    

    public LambdaQueryWrapperX<T> eqIfPresent(SFunction<T, ?> column, Object val) {
        return super.eq(isPresent(val), column, val);
    }

    public LambdaQueryWrapperX<T> neIfPresent(SFunction<T, ?> column, Object val) {
        return super.ne(isPresent(val), column, val);
    }

    public LambdaQueryWrapperX<T> gtIfPresent(SFunction<T, ?> column, Object val) {
        return super.gt(isPresent(val), column, val);
    }

    public LambdaQueryWrapperX<T> geIfPresent(SFunction<T, ?> column, Object val) {
        return super.ge(isPresent(val), column, val);
    }

    public LambdaQueryWrapperX<T> ltIfPresent(SFunction<T, ?> column, Object val) {
        return super.lt(isPresent(val), column, val);
    }

    public LambdaQueryWrapperX<T> leIfPresent(SFunction<T, ?> column, Object val) {
        return super.le(isPresent(val), column, val);
    }

    public LambdaQueryWrapperX<T> likeIfPresent(SFunction<T, ?> column, Object val) {
        return super.like(isPresent(val), column, val);
    }

    public LambdaQueryWrapperX<T> notLikeIfPresent(SFunction<T, ?> column, Object val) {
        return super.notLike(isPresent(val), column, val);
    }

    public LambdaQueryWrapperX<T> likeLeftIfPresent(SFunction<T, ?> column, Object val) {
        return super.likeLeft(isPresent(val), column, val);
    }

    public LambdaQueryWrapperX<T> likeRightIfPresent(SFunction<T, ?> column, Object val) {
        return super.likeRight(isPresent(val), column, val);
    }

    public LambdaQueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Object... values) {
        return super.in(isPresent(values), column, values);
    }

    public LambdaQueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Collection<?> values) {
        return super.in(isPresent(values), column, values);
    }

    public LambdaQueryWrapperX<T> notInIfPresent(SFunction<T, ?> column, Object... values) {
        return super.notIn(isPresent(values), column, values);
    }

    public LambdaQueryWrapperX<T> notInIfPresent(SFunction<T, ?> column, Collection<?> values) {
        return super.notIn(isPresent(values), column, values);
    }

    public LambdaQueryWrapperX<T> betweenIfPresent(SFunction<T, ?> column,Object val1, Object val2) {
        return super.between((isPresent(val1) && isPresent(val2)), column, val1, val2);
    }
    

    /**
     * 当前条件只是否非null，且不为空
     * @param obj 值
     * @return boolean 不为空返回true
     */
    @SuppressWarnings("rawtypes")
    private boolean isPresent(Object obj) {
        if (null == obj) {
            return false;
        }
        else if (obj instanceof CharSequence) {
            // 字符串比较特殊，如果是空字符串也不行
            return StrUtil.isNotBlank((CharSequence) obj);
        }
        else if (obj instanceof Collection) {
            return CollectionUtil.isNotEmpty((Collection) obj);
        }
        if (obj.getClass().isArray()) {
            return ArrayUtil.isNotEmpty(obj);
        }
        return true;
    }
}
