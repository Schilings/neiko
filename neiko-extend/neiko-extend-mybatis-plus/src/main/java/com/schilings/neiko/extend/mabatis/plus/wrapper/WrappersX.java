package com.schilings.neiko.extend.mabatis.plus.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.schilings.neiko.extend.mabatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.extend.mabatis.plus.wrapper.join.NeikoQueryWrapper;
import com.schilings.neiko.extend.mabatis.plus.wrapper.query.LambdaQueryWrapperX;

/**
 * <pre>
 * <p>{@link Wrappers}无法继承</p>
 * </pre>
 * @author Schilings
*/
public final class WrappersX {

    /**
     * WrappersX.<UserDO>queryJoin()
     */
    public static <T> NeikoQueryWrapper<T> queryJoin() {
        return new NeikoQueryWrapper<>();
    }

    /**
     * WrappersX.<UserDO>lambdaJoin()
     */
    public static <T> NeikoLambdaQueryWrapper<T> lambdaQueryJoin() {
        return new NeikoLambdaQueryWrapper<>();
    }


    /**
     * 获取 LambdaQueryWrapperX&lt;T&gt;
     *
     * @param <T> 实体类泛型
     * @return LambdaQueryWrapperX&lt;T&gt;
     */
    public static <T> LambdaQueryWrapperX<T> lambdaQueryX() {
        return new LambdaQueryWrapperX<>();
    }

    /**
     * 获取 LambdaQueryWrapperX&lt;T&gt;
     *
     * @param entity 实体类
     * @param <T>    实体类泛型
     * @return LambdaQueryWrapperX&lt;T&gt;
     */
    public static <T> LambdaQueryWrapperX<T> lambdaQueryX(T entity) {
        return new LambdaQueryWrapperX<>(entity);
    }

    /**
     * 获取 LambdaQueryWrapperX&lt;T&gt;
     *
     * @param entityClass 实体类class
     * @param <T>         实体类泛型
     * @return LambdaQueryWrapperX&lt;T&gt;
     * @since 3.3.1
     */
    public static <T> LambdaQueryWrapperX<T> lambdaQueryX(Class<T> entityClass) {
        return new LambdaQueryWrapperX<>(entityClass);
    }
}
