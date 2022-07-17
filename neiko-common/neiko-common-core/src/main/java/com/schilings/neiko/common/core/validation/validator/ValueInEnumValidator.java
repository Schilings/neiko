package com.schilings.neiko.common.core.validation.validator;


import com.schilings.neiko.common.core.validation.annotation.ValueInEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import javax.validation.ConstraintDefinitionException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * <pre>{@code
 *      
 * }
 * <p>@ValueInEnum的校验器</p>
 * <p>这个校验器暂时有局限性，就是指定的枚举类型的变量可能只能有一个，因为下面要把枚举类型解析成基本类型</p>
 * </pre>
 * @author Schilings
*/
@Slf4j
public class ValueInEnumValidator implements ConstraintValidator<ValueInEnum,Object> {

    private Class<?>[] targetEnum;
    private boolean allowNull;
    private String checkMethod;

    /**
     * 初始化
     * @param constraintAnnotation
     */
    @Override
    public void initialize(ValueInEnum constraintAnnotation) {
        targetEnum = constraintAnnotation.enumClass();
        allowNull = constraintAnnotation.allowNull();
        checkMethod = constraintAnnotation.method();
    }

    /**
     * 判断传入的参数是否有效
     * @param value 传入值
     * @param context 
     * @return
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        //如果为null，则由allowNull决定
        if (value == null) {
            return allowNull;
        }
        for (Class<?> eClass : targetEnum) {
            // 将指定的包装类转换为其对应的原始类
            // 原始类型（ Boolean 、 Byte 、 Character 、 Short 、 Integer 、 Long 、 Double 、 Float ）的处理
            // 即解析枚举类型成基本类型，用于执行静态方法直到方法参数类型
            //除非你自己在枚举类型写一个其他静态方法，不然默认的valueOf(String name)只能通过枚举类型名传参
            Class<?> clazz = ClassUtils.isPrimitiveWrapper(value.getClass()) ? 
                    ClassUtils.wrapperToPrimitive(value.getClass()) : value.getClass();

            try {
                //执行枚举类型的静态方法
                //执行的类eClass 执行的方法checkMethod，方法参数new Object[] { value }，方法参数类型new Class[] { clazz }
                Object enumInstance = MethodUtils.invokeStaticMethod(eClass, checkMethod, new Object[] { value },
                        new Class[] { clazz });
                return enumInstance != null;
            }
            catch (NoSuchMethodException ex) {
                //封装成校验异常，抛出
                throw new ConstraintDefinitionException(ex);
            }
            catch (Exception ex) {
                log.warn("check enum error: ", ex);
                return false;
            }
        }
        return false;
    }
}
