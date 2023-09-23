package com.schilings.neiko.common.core.validation.annotation;

import com.schilings.neiko.common.core.validation.validator.ValueInIntsValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <pre>{@code
 *
 * }
 * <p></p>
 * </pre>
 * @author Schilings
 */
@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(ValueInInts.List.class)
@Constraint(validatedBy = { ValueInIntsValidator.class })
@Documented
public @interface ValueInInts {

	String message() default "value must match one of the values in the list: {value}";

	int[] value();

	/**
	 * 允许值为 null, 默认不允许
	 */
	boolean allowNull() default false;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Documented
	@interface List {

		ValueInInts[] value();

	}

}
