package com.abc.xyz.PetStore.controllers;

import com.abc.xyz.PetStore.constants.ExceptionConstants;
import com.abc.xyz.PetStore.exceptions.PetException;
import com.abc.xyz.PetStore.models.PetDto;
import com.abc.xyz.PetStore.services.PetService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet-inventory/v1")
@Slf4j
@AllArgsConstructor
public class PetController {

    private final PetService petService;

    // below constructor DI can be achieved by using Lombok's Constructor annotation.
    // It will do the same
/*
    public PetController(PetService petService) {
        this.petService = petService;
    }*/


    @PostMapping(value = "/pet/{petId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> uploadImage(@PathVariable String petId, @RequestHeader String transactionId) {
        return new ResponseEntity<>("This feature is not yet available ", HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/pet")
    public ResponseEntity<PetDto> createPet(@RequestBody @Valid PetDto petDto, @RequestHeader String transactionId) {
        log.info("transactionId: {}, In PetController:: createPet(): {}", transactionId, petDto.toString());
        return petService.createPet(petDto, transactionId);
    }

    @GetMapping("/pet/findByStatus")
    public ResponseEntity<List<PetDto>> findByStatus(@RequestParam String status, @RequestHeader String transactionId) {
        log.info("transactionId: {}, In PetController:: findByStatus(): {}", transactionId, status);
        return petService.findPetsByStatus(status, transactionId);
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<PetDto> findPetById(@PathVariable String petId, @RequestHeader String transactionId) {
        log.info("transactionId: {}, In PetController:: findPetById(): {}", transactionId, petId);
        return petService.findPetById(parsePetId(petId, transactionId), transactionId);
    }

    @PutMapping("/pet")
    public ResponseEntity<PetDto> updatePet(@RequestBody @Valid PetDto petDto, @RequestHeader String transactionId) {
        log.info("transactionId: {}, In PetController:: updatePet(): {}", transactionId, petDto);
        return petService.updatePet(petDto, transactionId);
    }

    @DeleteMapping("/pet/{petId}")
    public ResponseEntity deletePetById(@PathVariable String petId, @RequestHeader String transactionId) {
        log.info("transactionId: {}, In PetController:: deletePetById(): {}", transactionId, petId);
        return petService.deletePetById(parsePetId(petId, transactionId), transactionId);
    }

    @PostMapping("/pet/{petId}")
    public ResponseEntity<String> UpdatePetWithFormData() {
        return new ResponseEntity("This feature is not yet available ", HttpStatus.NOT_IMPLEMENTED);
    }

    private Integer parsePetId(final String petId, final String transactionId) {
        try {
            return Integer.valueOf(petId);
        } catch (NumberFormatException exception) {
            log.error("transactionId: {}, Error In PetController :: parsePetId() for petId: {} :: Exception: {}", transactionId, petId, exception);
            throw new PetException(ExceptionConstants.INVALID_PET_ID_FORMAT, HttpStatus.BAD_REQUEST);
        }

    }

}
