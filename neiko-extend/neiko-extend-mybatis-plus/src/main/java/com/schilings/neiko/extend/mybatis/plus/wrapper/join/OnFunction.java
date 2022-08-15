package com.schilings.neiko.extend.mybatis.plus.wrapper.join;

@FunctionalInterface
public interface OnFunction {

    NeikoLambdaQueryWrapper<?> apply(NeikoLambdaQueryWrapper<?> wrapper);
}
