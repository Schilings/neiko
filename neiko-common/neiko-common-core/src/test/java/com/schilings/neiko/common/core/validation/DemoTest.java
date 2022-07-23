package com.schilings.neiko.common.core.validation;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.cache.annotation.Cacheable;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <pre>{@code
 *      
 * }
 * <p>测试自定义校验注释</p>
 * </pre>
 * @author Schilings
*/
@Slf4j
public class DemoTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        HibernateValidatorConfiguration configure = Validation.byProvider(HibernateValidator.class).configure();
        configure.defaultLocale(Locale.US);
        ValidatorFactory factory = configure.buildValidatorFactory();
        validator = factory.getValidator();
    }



    @Test
    void demoIsValid() {

        System.out.println(StatusEnum.valueOf("SUCCESS"));
        System.out.println("==========================");
        for (StatusEnum value : StatusEnum.values()) {
            System.out.println(value);
        }
        System.out.println("==========================");
        
        
        
        Demo demo = new Demo(1, "SUCCESS", 10, 10, null);
        Set<ConstraintViolation<Demo>> constraintViolations = validator.validate(demo);
        for (ConstraintViolation<Demo> violation : constraintViolations) {
            System.out.println(violation.getMessage());
        }
        assertEquals(0, constraintViolations.size());
    }

}
