package com.schilings.neiko.common.core.validation.validator;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.number.InfinityNumberComparatorHelper;

/**
 * <pre>
 * <p></p>
 * </pre>
 * @author Schilings
*/
@Slf4j
public class LessValidatorForDouble extends AbstractLessValidator<Double>{
    @Override
    protected int compare(Double number) {
        return NumberComparatorHelper.compare( number, compareValue, InfinityNumberComparatorHelper.LESS_THAN );
    }
}
