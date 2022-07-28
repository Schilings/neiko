package com.schilings.neiko.common.core.validation.validator;


import com.schilings.neiko.common.core.validation.helper.NumberComparatorHelper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.number.InfinityNumberComparatorHelper;

@Slf4j
public class LessValidatorForNumber extends AbstractLessValidator<Number>{
    @Override
    protected int compare(Number number) {
        return NumberComparatorHelper.compare(number, compareValue, InfinityNumberComparatorHelper.LESS_THAN);
    }
}
