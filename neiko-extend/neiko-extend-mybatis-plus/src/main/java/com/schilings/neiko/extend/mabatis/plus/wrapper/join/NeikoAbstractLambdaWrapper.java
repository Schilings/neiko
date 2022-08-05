package com.schilings.neiko.extend.mabatis.plus.wrapper.join;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.schilings.neiko.extend.mabatis.plus.constants.Constant;
import com.schilings.neiko.extend.mabatis.plus.utils.LambdaUtils;
import com.schilings.neiko.extend.mabatis.plus.wrapper.query.LambdaQueryWrapperX;

import java.util.*;

import static java.util.stream.Collectors.joining;

/**
 * Lambda 语法使用 Wrapper
 * <p>统一处理解析 lambda 获取 column</p>
 *
 * @author Schilings
 */
public abstract class NeikoAbstractLambdaWrapper<T,Children extends NeikoAbstractLambdaWrapper<T,Children>> 
        extends NeikoAbstractWrapper<T, Children> {

    /**
     * 关联的表
     */
    protected Map<Class<?>, Integer> subTable = new HashMap<>();

    /**
     * 缓存字段
     */
    protected Map<Class<?>, Map<String, ColumnCache>> columnMap = new HashMap<>();

    @Override
    protected <X> String columnToString(X column) {
        return columnToString((SFunction<?, ?>) column);
    }

    @Override
    @SafeVarargs
    protected final <X> String columnsToString(X... columns) {
        return Arrays.stream(columns).map(i -> columnToString((SFunction<?, ?>) i)).collect(joining(StringPool.COMMA));
    }

    protected String columnToString(SFunction<?, ?> column) {
        return Constant.TABLE_ALIAS + getDefault(subTable.get(LambdaUtils.getEntityClass(column))) + StringPool.DOT +
                getCache(column).getColumn();
    }

    protected ColumnCache getCache(SFunction<?, ?> fn) {
        Class<?> aClass = LambdaUtils.getEntityClass(fn);
        Map<String, ColumnCache> cacheMap = columnMap.get(aClass);
        if (cacheMap == null) {
            cacheMap = LambdaUtils.getColumnMap(aClass);
            columnMap.put(aClass, cacheMap);
        }
        return cacheMap.get(LambdaUtils.formatKey(LambdaUtils.getName(fn)));
    }

    protected String getDefault(Integer i) {
        if (Objects.nonNull(i)) {
            return i.toString();
        }
        return StringPool.EMPTY;
    }

    
    public Children eqIfPresent(SFunction<T, ?> column, Object val) {
        return super.eq(isPresent(val), column, val);
    }

    public Children neIfPresent(SFunction<T, ?> column, Object val) {
        return super.ne(isPresent(val), column, val);
    }

    public Children gtIfPresent(SFunction<T, ?> column, Object val) {
        return super.gt(isPresent(val), column, val);
    }

    public Children geIfPresent(SFunction<T, ?> column, Object val) {
        return super.ge(isPresent(val), column, val);
    }

    public Children ltIfPresent(SFunction<T, ?> column, Object val) {
        return super.lt(isPresent(val), column, val);
    }

    public Children leIfPresent(SFunction<T, ?> column, Object val) {
        return super.le(isPresent(val), column, val);
    }

    public Children likeIfPresent(SFunction<T, ?> column, Object val) {
        return super.like(isPresent(val), column, val);
    }

    public Children notLikeIfPresent(SFunction<T, ?> column, Object val) {
        return super.notLike(isPresent(val), column, val);
    }

    public Children likeLeftIfPresent(SFunction<T, ?> column, Object val) {
        return super.likeLeft(isPresent(val), column, val);
    }

    public Children likeRightIfPresent(SFunction<T, ?> column, Object val) {
        return super.likeRight(isPresent(val), column, val);
    }

    public Children inIfPresent(SFunction<T, ?> column, Object... values) {
        return super.in(isPresent(values), column, values);
    }

    public Children inIfPresent(SFunction<T, ?> column, Collection<?> values) {
        return super.in(isPresent(values), column, values);
    }

    public Children notInIfPresent(SFunction<T, ?> column, Object... values) {
        return super.notIn(isPresent(values), column, values);
    }

    public Children notInIfPresent(SFunction<T, ?> column, Collection<?> values) {
        return super.notIn(isPresent(values), column, values);
    }

    public Children betweenIfPresent(SFunction<T, ?> column,Object val1, Object val2) {
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
