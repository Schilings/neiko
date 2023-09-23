package com.schilings.neiko.common.core.validation.validator;

import com.schilings.neiko.common.core.validation.annotation.ValueInInts;

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
public class ValueInIntsValidator implements ConstraintValidator<ValueInInts, Integer> {

	private int[] ints;

	private boolean allowNull;

	@Override
	public void initialize(ValueInInts constraintAnnotation) {
		ints = constraintAnnotation.value();
		allowNull = constraintAnnotation.allowNull();
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if (value == null) {
			return allowNull;
		}
		for (int anInt : ints) {
			if (anInt == value) {
				return true;
			}
		}
		return false;
	}

}
