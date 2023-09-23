package com.schilings.neiko.common.core.validation.validator;

import com.schilings.neiko.common.core.validation.annotation.Greater;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
public abstract class AbstractGreaterValidator<T> implements ConstraintValidator<Greater, T> {

	protected long compareValue;

	private boolean equal;

	@Override
	public void initialize(Greater constraintAnnotation) {
		this.compareValue = constraintAnnotation.value();
		this.equal = constraintAnnotation.equal();
	}

	@Override
	public boolean isValid(T value, ConstraintValidatorContext context) {
		// null values are valid
		if (value == null) {
			return true;
		}
		if (equal) {
			return compare(value) >= 0;
		}
		else {
			return compare(value) > 0;
		}
	}

	protected abstract int compare(T number);

}
