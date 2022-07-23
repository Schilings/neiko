package com.schilings.neiko.common.core.validation.validator;


import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * <pre>
 * <p></p>
 * </pre>
 * @author Schilings
*/
@Slf4j
public class LessValidatorForCharSequence extends AbstractLessValidator<CharSequence>{
    @Override
    protected int compare(CharSequence number) {
        try {
            return NumberComparatorHelper.compare(new BigDecimal( number.toString() ), compareValue);
        }catch (NumberFormatException nfe) {
            return -1;
        }
    }
}
