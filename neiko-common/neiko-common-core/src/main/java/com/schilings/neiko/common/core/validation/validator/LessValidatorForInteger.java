package com.schilings.neiko.common.core.validation.validator;

import com.schilings.neiko.common.core.validation.helper.NumberComparatorHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LessValidatorForInteger extends AbstractLessValidator<Integer> {

	@Override
	protected int compare(Integer number) {
		return NumberComparatorHelper.compare(number.longValue(), compareValue);
	}

}
