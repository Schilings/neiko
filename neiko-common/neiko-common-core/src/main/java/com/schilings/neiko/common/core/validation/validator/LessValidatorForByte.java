package com.schilings.neiko.common.core.validation.validator;


import com.schilings.neiko.common.core.validation.helper.NumberComparatorHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * <p></p>
 * </pre>
 * @author Schilings
*/
@Slf4j
public class LessValidatorForByte extends AbstractLessValidator<Byte>{
    @Override
    protected int compare(Byte number) {
        return NumberComparatorHelper.compare(number.longValue(), compareValue);
    }
}
