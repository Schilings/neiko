package com.schilings.neiko.common.core.validation.validator;

import com.schilings.neiko.common.core.validation.annotation.ValueInStrings;
import com.schilings.neiko.common.util.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * <pre>{@code
 *
 * }
 * <p></p>
 * </pre>
 *
 * @author Schilings
 */
public class ValueInStringsValidator implements ConstraintValidator<ValueInStrings, String> {

	private String[] stringList;

	private boolean allowBlank;

	@Override
	public void initialize(ValueInStrings constraintAnnotation) {
		stringList = constraintAnnotation.value();
		allowBlank = constraintAnnotation.allowBlank();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value)) {
			return allowBlank;
		}
		for (String strValue : stringList) {
			if (strValue.equals(value)) {
				return true;
			}
		}
		return false;
	}

}
