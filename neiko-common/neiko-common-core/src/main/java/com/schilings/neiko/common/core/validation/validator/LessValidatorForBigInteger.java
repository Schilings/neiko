package com.schilings.neiko.common.core.validation.validator;


import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

/**
 * <pre>
 * <p></p>
 * </pre>
 * @author Schilings
*/
@Slf4j
public class LessValidatorForBigInteger extends AbstractLessValidator<BigInteger> {
    @Override
    protected int compare(BigInteger number) {
        return NumberComparatorHelper.compare(number, compareValue);
    }
}
