package com.schilings.neiko.common.core.validation.validator;


import com.schilings.neiko.common.core.validation.helper.NumberComparatorHelper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.number.InfinityNumberComparatorHelper;

@Slf4j
public class LessValidatorForFloat extends AbstractLessValidator<Float>{
    @Override
    protected int compare(Float number) {
        return NumberComparatorHelper.compare(number, compareValue, InfinityNumberComparatorHelper.LESS_THAN);
    }
}
