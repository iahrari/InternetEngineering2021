package com.example.demo.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsEvenValidator implements ConstraintValidator<IsEven, Number> {
    @Override
    public void initialize(IsEven constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return value.doubleValue() % 2 == 0;
    }
}
