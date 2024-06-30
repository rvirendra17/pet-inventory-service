package com.abc.xyz.PetStore.services.impls;

import com.abc.xyz.PetStore.constants.ExceptionConstants;
import com.abc.xyz.PetStore.entities.Pet;
import com.abc.xyz.PetStore.entities.Tag;
import com.abc.xyz.PetStore.enums.PetStatus;
import com.abc.xyz.PetStore.exceptions.PetException;
import com.abc.xyz.PetStore.mappers.PetMapper;
import com.abc.xyz.PetStore.models.PetDto;
import com.abc.xyz.PetStore.repositories.PetRepository;
import com.abc.xyz.PetStore.services.PetService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class PetServiceImpl implements PetService {

    private PetRepository petRepository;

    private PetMapper petMapper;

    // Constructor based DI/Assured DI
    public PetServiceImpl(PetRepository petRepository, PetMapper petMapper) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
    }

    @Transactional
    @Override
    public ResponseEntity<PetDto> createPet(PetDto petDto, String transactionId) {
        Pet pet = mapToPet(petDto);

        if (petRepository.findByName(pet.getName()).isPresent()) {
            log.error("transactionId: {}, Error in creating a pet record for pet: {}", transactionId, pet.getName());
            throw new PetException(ExceptionConstants.PET_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
        }

        pet.getTags().stream().forEach(tag -> tag.setPet(pet)); // setting pet for every tag as JPA/Hibernate is unaware of the reference of pet in a tag
        Pet savedPet = petRepository.save(pet);
        log.info("transactionId: {}, Pet saved to database successfully: {}", transactionId, savedPet.getCategory());
        return new ResponseEntity<>(mapToPetDto(savedPet), HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<List<PetDto>> findPetsByStatus(String status, String transactionId) {

        // accept only unique status values
        Set<String> statuses = getStatusSet(status);
        Set<String> filteredStatuses = filterStatusValues(statuses);
        HttpStatus httpStatus = null;

        if (filteredStatuses.size() == 0) {
            log.error("transactionId: {}, Error in findPetsByStatus for status values: {}", transactionId, status);
            throw new PetException(ExceptionConstants.INVALID_STATUS_VALUES + status, HttpStatus.BAD_REQUEST);
        } else if (filteredStatuses.size() < statuses.size()) {
            log.warn("transactionId: {}, Some of the status values are invalid and response will be rendered for valid status values only: {}", transactionId, status);
            httpStatus = HttpStatus.PARTIAL_CONTENT;
        } else if (filteredStatuses.size() == statuses.size()) {
            log.info("transactionId: {},All the status values received in the query are VALID: {}", transactionId, status);
            httpStatus = HttpStatus.OK;
        }



        /*// Fetch all pets once from database so that no recurring transactions with database.
        // Review - Loading all pets into memory - This can overload memory (Heap/RAM) for large datasets
        //Be cautious with fetchAll for performance. Always keep BigData sets in mind while interacting with database
        List<Pet> allPets = petRepository.findAll();

        // Create a list to insert pets by required status
        List<Pet> petsByStatus = new ArrayList<>();

        // Review - iterating over entire database for times equal to number of status - Bad
        for (String petStatus : statuses) {

            // Review - streaming in loops that too streaming entire database and then filtering
            petsByStatus.addAll(allPets.stream()
                    .filter(pet -> pet.getStatus().equalsIgnoreCase(petStatus))
                    .toList());
        }

        // Review - so above code although is low in cognitive complexity, it is not performance effective, robust, scalable.
        //map the pets to PetDtos
        List<PetDto> petDtosByStatus = petMapper.toPetDtoList(petsByStatus);

        // Review Feedback - Above code consumes a lot of memory and has low performace
        // and hence low throughput.
        // Write a query that will give the required pets by all the status in oneshot from database
        // This will be more memory and performance optimized solution

        */

/*
        // Group the pets by status for more readability and accessibility

        Map<String, List<Pet>> petByStatusMap = petsByStatuses.stream().collect(Collectors.groupingBy(Pet::getStatus));
        List<Pet> petsByStatuses = petByStatusMap.values().stream().flatMap(List::stream).collect(Collectors.toList());*/

        Optional<List<Pet>> petsOptional = petRepository.findByStatusIn(filteredStatuses);
        if (petsOptional.isEmpty() || petsOptional.get().isEmpty()) {
            log.error("transactionId: {}, Error in findPetsByStatus, pets found for status values: {}", transactionId, status);
            throw new PetException(ExceptionConstants.NO_PETS_FOUND_BY_STATUS + statuses, HttpStatus.NOT_FOUND);
        }
        List<PetDto> petDtosByStatuses = petMapper.toPetDtoList(petsOptional.get());
        log.info("transactionId: {}, pets extracted successfully by status values: {}", transactionId, petDtosByStatuses);
        return new ResponseEntity<>(petDtosByStatuses, httpStatus);
    }

    @Override
    public ResponseEntity<PetDto> findPetById(Integer petId, String transactionId) {

        Optional<Pet> petOptional = petRepository.findByPetId(petId);
        if (petOptional.isEmpty()) {
            log.error("transactionId: {}, Error in findPetById, pet not found for the given id : {}", transactionId, petId);
            throw new PetException(ExceptionConstants.PET_NOT_FOUND_BY_ID + petId, HttpStatus.NOT_FOUND);
        }
        PetDto petDto = mapToPetDto(petOptional.get());
        log.info("transactionId: {} Pet found for the given id: {}", transactionId, petId);
        return new ResponseEntity<>(petDto, HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<PetDto> updatePet(PetDto petDto, String transactionId) {
        Pet pet = mapToPet(petDto);
        Optional<Pet> petOptional = petRepository.findByName(pet.getName());
        if (petOptional.isEmpty()) {
            log.error("transactionId: {} Error in updatePet, pet record not found for pet: {}", transactionId, pet.getName());
            throw new PetException(ExceptionConstants.UPDATE_FAILED_PET_NOT_FOUND + pet.getName(), HttpStatus.BAD_REQUEST);
        }

        /*
         * if save method does not find an entity id in the record, it will create a new record instead of updating it.
         * So setting the entity id for the pet record before saving it. So that it save method will update the record.
         * */
        pet.setId(petOptional.get().getId());
        petRepository.save(pet);
        log.info("transactionId: {} , Updated the pet record successfully for pet: {}", transactionId, pet.getName());
        return new ResponseEntity<>(petDto, HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity deletePetById(Integer petId, String transactionId) {
        Optional<Pet> petOptional = petRepository.findByPetId(petId);
        if (petOptional.isEmpty()) {
            log.error("transactionId: {}, Error in deletePetById, pet record not found for petId: ", transactionId, petId);
            throw new PetException(ExceptionConstants.PET_NOT_FOUND_BY_ID + petId, HttpStatus.NOT_FOUND);
        }
        petRepository.deleteByPetId(petId);
        log.info("transactionId: {} Pet record deleted successfully for petId: {}", transactionId, petId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    private Pet mapToPet(final PetDto petDto) {
        Pet mappedPet = null;
        try {
            mappedPet = petMapper.petDtoToPet(petDto);
            log.debug("Mapped pet: {}", mappedPet);
        } catch (Exception exception) {
            log.error("Exception in mapping to Pet: {}", exception);
            throw new RuntimeException("Exception in mapping to Pet");
        }
        return mappedPet;
    }

    private PetDto mapToPetDto(final Pet pet) {
        PetDto petDto = null;
        try {
            petDto = petMapper.petToPetDto(pet);
            log.debug("Mapped petDto: {}", petDto);
        } catch (Exception exception) {
            log.error("Exception in mapping to PetDto: {}", exception);
            throw new RuntimeException("Exception in mapping to PetDto");
        }
        return petDto;
    }

    private Set<String> getStatusSet(String status) {
        return (status == null || status.isEmpty()) ? Collections.emptySet() : new HashSet<>(Arrays.asList(status.split(",")));
    }

    private Set<String> filterStatusValues(Set<String> status) {

        if (status == null || status.isEmpty()) return Collections.emptySet();

        Set<String> filteredStatus = new HashSet<>();
        Set<String> petStatuses = new HashSet<>(PetStatus.getPetStatusMap().values());

        for (String petStatus : status) {
            if (petStatuses.contains(petStatus)) filteredStatus.add(petStatus);
        }

        return filteredStatus;
    }
}
