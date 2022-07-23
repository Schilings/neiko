package com.schilings.neiko.common.core.validation.validator;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LessValidatorForLong extends AbstractLessValidator<Long>{
    @Override
    protected int compare(Long number) {
        return NumberComparatorHelper.compare(number, compareValue);
    }
}
