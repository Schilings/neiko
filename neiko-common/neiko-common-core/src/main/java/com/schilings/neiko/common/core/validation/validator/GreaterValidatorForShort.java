package com.schilings.neiko.common.core.validation.validator;

import com.schilings.neiko.common.core.validation.helper.NumberComparatorHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * <p>检查正在验证的数字是否大于或大于等于指定的设定的值</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
public class GreaterValidatorForShort extends AbstractGreaterValidator<Short> {

	@Override
	protected int compare(Short number) {
		return NumberComparatorHelper.compare(number.longValue(), compareValue);
	}

}
