package com.schilings.neiko.common.core.validation.validator;


import com.schilings.neiko.common.core.validation.annotation.ValueInStrings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * <pre>{@code
 *      
 * }
 * <p></p>
 * </pre>
 * @author Schilings
*/
public class ValueInStringsValidator implements ConstraintValidator<ValueInStrings,String> {
    private String[] stringList;

    private boolean allowNull;
    
    @Override
    public void initialize(ValueInStrings constraintAnnotation) {
        stringList = constraintAnnotation.value();
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return allowNull;
        }
        for (String strValue : stringList) {
            if (strValue.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
