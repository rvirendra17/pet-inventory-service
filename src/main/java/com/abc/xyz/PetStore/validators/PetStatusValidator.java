package com.abc.xyz.PetStore.validators;

import com.abc.xyz.PetStore.validators.impls.PetStatusValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = {PetStatusValidatorImpl.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PetStatusValidator {

    String message() default "Pet status is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
