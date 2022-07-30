package com.schilings.neiko.common.core.validation.validator;

import com.schilings.neiko.common.core.validation.annotation.Less;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author Schilings
 */
public abstract class AbstractLessValidator<T> implements ConstraintValidator<Less, T> {

	protected long compareValue;

	private boolean equal;

	@Override
	public void initialize(Less constraintAnnotation) {
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
			return compare(value) <= 0;
		}
		else {
			return compare(value) < 0;
		}
	}

	protected abstract int compare(T number);

}
