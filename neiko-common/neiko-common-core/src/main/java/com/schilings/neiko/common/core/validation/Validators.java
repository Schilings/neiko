package com.schilings.neiko.common.core.validation;

import cn.hutool.extra.validation.BeanValidationResult;
import cn.hutool.extra.validation.ValidationUtil;
import com.schilings.neiko.common.util.spring.SpringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.validation.*;
import java.util.Set;

/**
 * 手动校验工具
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Validators {

	/**
	 * 默认{@link Validator} 对象
	 */
	private static Validator validator;

	static {
		validator = SpringUtils.getBean(Validator.class);
		if (validator == null) {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();
		}
	}

	/**
	 * 获取原生{@link Validator} 对象
	 * @return {@link Validator} 对象
	 */
	public static Validator getValidator() {
		return validator;
	}

	/**
	 * Validates all constraints on {@code object}.
	 * @param object object to validate
	 * @param <T> the type of the object to validate
	 * @return constraint violations or an empty set if none
	 * @throws IllegalArgumentException if object is {@code null} or if {@code null} is
	 * passed to the varargs groups
	 * @throws ValidationException if a non recoverable error happens during the
	 * validation process
	 */
	public static <T> Set<ConstraintViolation<T>> validate(T object) {
		return validator.validate(object);
	}

	/**
	 * 校验对象
	 * @param <T> Bean类型
	 * @param bean bean
	 * @param groups 校验组
	 * @return {@link Set}
	 */
	public static <T> Set<ConstraintViolation<T>> validate(T bean, Class<?>... groups) {
		return validator.validate(bean, groups);
	}

	/**
	 * 校验bean的某一个属性
	 * @param <T> Bean类型
	 * @param bean bean
	 * @param propertyName 属性名称
	 * @param groups 验证分组
	 * @return {@link Set}
	 */
	public static <T> Set<ConstraintViolation<T>> validateProperty(T bean, String propertyName, Class<?>... groups) {
		return validator.validateProperty(bean, propertyName, groups);
	}

	/**
	 * 校验对象
	 * @param <T> Bean类型
	 * @param bean bean
	 * @return {@link BeanValidationResult}
	 */
	public static <T> BeanValidationResult warpValidate(T bean) {
		return warpBeanValidationResult(validate(bean));
	}

	/**
	 * 校验对象
	 * @param <T> Bean类型
	 * @param bean bean
	 * @param groups 校验组
	 * @return {@link BeanValidationResult}
	 */
	public static <T> BeanValidationResult warpValidate(T bean, Class<?>... groups) {
		return warpBeanValidationResult(validate(bean, groups));
	}

	/**
	 * 校验bean的某一个属性
	 * @param <T> bean类型
	 * @param bean bean
	 * @param propertyName 属性名称
	 * @param groups 验证分组
	 * @return {@link BeanValidationResult}
	 */
	public static <T> BeanValidationResult warpValidateProperty(T bean, String propertyName, Class<?>... groups) {
		return warpBeanValidationResult(validateProperty(bean, propertyName, groups));
	}

	/**
	 * 包装校验结果
	 * @param constraintViolations 校验结果集
	 * @return {@link BeanValidationResult}
	 */
	private static <T> BeanValidationResult warpBeanValidationResult(Set<ConstraintViolation<T>> constraintViolations) {
		BeanValidationResult result = new BeanValidationResult(constraintViolations.isEmpty());
		for (ConstraintViolation<T> constraintViolation : constraintViolations) {
			BeanValidationResult.ErrorMessage errorMessage = new BeanValidationResult.ErrorMessage();
			errorMessage.setPropertyName(constraintViolation.getPropertyPath().toString());
			errorMessage.setMessage(constraintViolation.getMessage());
			errorMessage.setValue(constraintViolation.getInvalidValue());
			result.addErrorMessage(errorMessage);
		}
		return result;
	}

}
