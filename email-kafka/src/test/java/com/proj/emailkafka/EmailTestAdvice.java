package com.proj.emailkafka;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

public class EmailTestAdvice {

    public static <T> Set<ConstraintViolation<T>> getViolation(T testedClass) {
        return Validation.buildDefaultValidatorFactory()
                .getValidator()
                .validate(testedClass);
    }
}
