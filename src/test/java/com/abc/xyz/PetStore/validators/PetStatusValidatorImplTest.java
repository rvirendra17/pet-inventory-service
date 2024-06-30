package com.abc.xyz.PetStore.validators;

import com.abc.xyz.PetStore.enums.PetStatus;
import com.abc.xyz.PetStore.validators.impls.PetStatusValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.HashSet;
import java.util.Set;


public class PetStatusValidatorImplTest {

    private Set<String> validPetStatuses;
    private String inValidPetStatus;

    private PetStatusValidatorImpl petStatusValidatorImpl;

    @BeforeEach
    public void setup() {

        petStatusValidatorImpl = new PetStatusValidatorImpl();
        validPetStatuses = new HashSet<>(PetStatus.getPetStatusMap().values());
        inValidPetStatus = "ABC-XYZ";
    }

    @ParameterizedTest
    @EnumSource(PetStatus.class)
    public void isValidTest(PetStatus petStatus) {
        Assertions.assertTrue(petStatusValidatorImpl.isValid(petStatus.getPetStatus(), null));
    }

    @Test
    public void isInvalidTest() {
        Assertions.assertFalse(petStatusValidatorImpl.isValid(inValidPetStatus, null));
    }
}
