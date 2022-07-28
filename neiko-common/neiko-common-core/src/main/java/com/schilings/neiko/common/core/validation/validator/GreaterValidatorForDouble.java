package com.schilings.neiko.common.core.validation.validator;


import com.schilings.neiko.common.core.validation.helper.NumberComparatorHelper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.number.InfinityNumberComparatorHelper;


/**
 * <pre>
 * <p>检查正在验证的数字是否大于或等于指定的最小值。</p>
 * </pre>
 * @author Schilings
*/
@Slf4j
public class GreaterValidatorForDouble extends AbstractGreaterValidator<Double> {
    @Override
    protected int compare(Double number) {
        return NumberComparatorHelper.compare( number, compareValue, InfinityNumberComparatorHelper.LESS_THAN );
    }
}
