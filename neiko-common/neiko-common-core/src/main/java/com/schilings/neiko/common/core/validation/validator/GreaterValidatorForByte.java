package com.schilings.neiko.common.core.validation.validator;


import com.schilings.neiko.common.core.validation.helper.NumberComparatorHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * <p>检查正在验证的数字是否大于或等于指定的最小值。</p>
 * </pre>
 * @author Schilings
*/
@Slf4j
public class GreaterValidatorForByte extends AbstractGreaterValidator<Byte>{
    @Override
    protected int compare(Byte number) {
        return NumberComparatorHelper.compare(number.longValue(), compareValue);
    }
}
