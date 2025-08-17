package com.nhngcmnh.example.identity_service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

    private int min;
    private String message;

    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Not validating null values
        }
        boolean valid = value.isBefore(LocalDate.now().minusYears(min));
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message.replace("{min}", String.valueOf(min))).addConstraintViolation();
        }
        return valid;
    }
}
