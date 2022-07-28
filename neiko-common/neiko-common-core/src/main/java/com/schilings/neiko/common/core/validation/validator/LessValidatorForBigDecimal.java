package com.schilings.neiko.common.core.validation.validator;


import com.schilings.neiko.common.core.validation.helper.NumberComparatorHelper;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * <pre>
 * <p></p>
 * </pre>
 * @author Schilings
*/

@Slf4j
public class LessValidatorForBigDecimal extends AbstractLessValidator<BigDecimal> {
    @Override
    protected int compare(BigDecimal number) {
        return NumberComparatorHelper.compare(number, compareValue);
    }
}
