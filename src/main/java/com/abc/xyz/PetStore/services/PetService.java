package com.abc.xyz.PetStore.services;

import com.abc.xyz.PetStore.entities.Pet;
import com.abc.xyz.PetStore.models.PetDto;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface PetService {

    // JSON Operations
    ResponseEntity<PetDto> createPet(PetDto petDto, String transactionId);

    ResponseEntity<List<PetDto>> findPetsByStatus(String status, String transactionId);

    /*Deprecated
        ResponseEntity<List<PetDto>> findPetsByTags(String tags);*/
    ResponseEntity<PetDto> findPetById(Integer petId, String transactionId);

    ResponseEntity<PetDto> updatePet(PetDto petDto, String transactionId);

    ResponseEntity deletePetById(Integer petId, String transactionId);

    // Form-data operations


}
