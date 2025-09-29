package com.chromeckap.backend.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DeadlineValidator.class)
@Documented
public @interface ValidDeadline {

    String message() default "{bet.deadline.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
