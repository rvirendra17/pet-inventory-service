package com.abc.xyz.PetStore.validators.impls;

import com.abc.xyz.PetStore.enums.PetStatus;
import com.abc.xyz.PetStore.validators.PetStatusValidator;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PetStatusValidatorImpl implements ConstraintValidator<PetStatusValidator, String> {
    @Override
    public boolean isValid(String status, ConstraintValidatorContext constraintValidatorContext) {
        return !StringUtils.isEmpty(status) && PetStatus.getPetStatusMap() != null && PetStatus.getPetStatusMap().containsValue(status);
    }


}
