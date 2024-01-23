package com.proj.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class TestAdvice {

    public static <T> Set<ConstraintViolation<T>> getViolation(T testedClass) {
        return Validation.buildDefaultValidatorFactory()
                .getValidator()
                .validate(testedClass);
    }

    public static ResultMatcher getErrorModelAttributes() {
        return model().attributeExists("errorResponse", "formatter");
    }

    public static ResultMatcher getErrorViewName() {
        return view().name("error");
    }
}
