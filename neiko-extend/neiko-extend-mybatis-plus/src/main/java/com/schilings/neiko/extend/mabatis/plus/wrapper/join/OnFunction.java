package com.schilings.neiko.extend.mabatis.plus.wrapper.join;

@FunctionalInterface
public interface OnFunction {

    NeikoLambdaQueryWrapper<?> apply(NeikoLambdaQueryWrapper<?> wrapper);
}
