package com.schilings.neiko.common.core.validation.validator;

import com.schilings.neiko.common.core.validation.helper.NumberComparatorHelper;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * <pre>
 * <p>检查正在验证的字符序列（例如字符串）是否代表一个数字，并且其值大于或等于指定的最小值。</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
public class GreaterValidatorForCharSequence extends AbstractGreaterValidator<CharSequence> {

	@Override
	protected int compare(CharSequence number) {
		try {
			return NumberComparatorHelper.compare(new BigDecimal(number.toString()), compareValue);
		}
		catch (NumberFormatException nfe) {
			return -1;
		}

	}

}
