package com.schilings.neiko.common.core.validation.validator;


import com.schilings.neiko.common.core.validation.helper.NumberComparatorHelper;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
/**
 * <pre>
 * <p>检查正在验证的数字是否大于或等于指定的设定的值</p>
 * </pre>
 * @author Schilings
 */
@Slf4j
public class GreaterValidatorForBigInteger extends AbstractGreaterValidator<BigInteger> {
    @Override
    protected int compare(BigInteger number) {
        return NumberComparatorHelper.compare(number, compareValue);
    }
}
