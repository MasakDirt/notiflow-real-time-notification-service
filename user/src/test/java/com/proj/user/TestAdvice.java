package com.proj.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;

import java.util.Set;

public class TestAdvice {

    public static <T> Set<ConstraintViolation<T>> getViolation(T testedClass) {
        return Validation.buildDefaultValidatorFactory()
                .getValidator()
                .validate(testedClass);
    }
}
