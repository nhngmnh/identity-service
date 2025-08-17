package com.nhngcmnh.example.identity_service.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
   validatedBy = {DobValidator.class}
)
public @interface DobConstraint {
    String message() default "INVALID_DOB";
    
    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
