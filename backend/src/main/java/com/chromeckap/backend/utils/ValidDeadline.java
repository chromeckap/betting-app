package com.chromeckap.backend.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DeadlineValidator.class)
@Documented
public @interface ValidDeadline {

    String message() default "Deadline must be at least 5 minutes in the future.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
