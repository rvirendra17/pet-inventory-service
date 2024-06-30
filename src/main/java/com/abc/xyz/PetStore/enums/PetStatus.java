package com.abc.xyz.PetStore.enums;

import java.util.EnumMap;

public enum PetStatus {

    AVAILABLE("available"),
    PENDING("pending"),
    SOLD("sold");


    private final String petStatus;

    PetStatus(String petStatus) {
        this.petStatus = petStatus;
    }

    public String getPetStatus() {
        return this.petStatus;
    }

    public static EnumMap<PetStatus, String> getPetStatusMap() {
        EnumMap<PetStatus, String> petStatusEnumMap = new EnumMap<>(PetStatus.class);
        for (PetStatus status : PetStatus.values()) {
            petStatusEnumMap.put(status, status.getPetStatus());
        }
        return petStatusEnumMap;
    }
}
