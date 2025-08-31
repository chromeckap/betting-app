package com.chromeckap.backend.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class DeadlineValidator implements ConstraintValidator<ValidDeadline, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime deadline, ConstraintValidatorContext constraintValidatorContext) {
        if (deadline == null)
            return false;

        return deadline.isAfter(LocalDateTime.now().plusMinutes(5));
    }
}
