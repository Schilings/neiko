package com.schilings.neiko.common.core.validation.validator;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.number.InfinityNumberComparatorHelper;

/**
 * <pre>
 * <p>检查正在验证的数字是否大于或大于等于指定的设定的值</p>
 * </pre>
 * @author Schilings
 */
@Slf4j
public class GreaterValidatorForNumber extends AbstractGreaterValidator<Number>{
    @Override
    protected int compare(Number number) {
        return NumberComparatorHelper.compare(number, compareValue, InfinityNumberComparatorHelper.LESS_THAN);
    }
}
